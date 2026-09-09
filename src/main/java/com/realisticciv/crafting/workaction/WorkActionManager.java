package com.realisticciv.crafting.workaction;

import com.realisticciv.RealisticCiv;
import com.realisticciv.content.block.GroundResourceBlock;
import com.realisticciv.crafting.operation.OperationDefinition;
import com.realisticciv.crafting.operation.OperationCategory;
import com.realisticciv.crafting.operation.OperationMatch;
import com.realisticciv.crafting.operation.OperationOutputDefinition;
import com.realisticciv.registry.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Server-authoritative runtime manager for timed player work.
 *
 * <p>v0.1.5 keeps the intentionally narrow but reusable foundation:
 * one consumed operation input, one persistent tool capability, optional
 * participation by one Ground Resource, progress/cancellation, and fixed
 * operation outputs. The same runtime now executes both primitive knapping
 * and inventory Hand Crafting. Workstations, settlers, skills, quality, and
 * richer reservation types should extend this layer rather than inventing
 * separate timer systems.</p>
 */
public final class WorkActionManager {
    private static final double MAX_STATIONARY_DISTANCE_SQR = 0.15D * 0.15D;
    private static final int PROGRESS_SEGMENTS = 10;
    private static final int PROGRESS_UPDATE_INTERVAL_TICKS = 2;
    private static final int KNAPPING_FIRST_STRIKE_TICK = 8;
    private static final int KNAPPING_STRIKE_INTERVAL_TICKS = 16;

    private static final Map<UUID, ActiveWorkAction> ACTIVE_ACTIONS = new HashMap<>();
    private static final Map<GroundReservationKey, UUID> GROUND_RESERVATIONS = new HashMap<>();
    private static boolean initialized;

    private WorkActionManager() {
    }

    public static void initialize() {
        if (initialized) {
            return;
        }

        ServerTickEvents.END_SERVER_TICK.register(WorkActionManager::tickServer);
        ServerPlayerEvents.LEAVE.register(player -> cancel(player, true, true));
        ServerLifecycleEvents.SERVER_STOPPING.register(WorkActionManager::cancelAllForShutdown);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            ACTIVE_ACTIONS.clear();
            GROUND_RESERVATIONS.clear();
        });

        initialized = true;
        RealisticCiv.LOGGER.info("Initialized server-authoritative WorkAction manager");
    }

    public static boolean isWorking(ServerPlayer player) {
        return ACTIVE_ACTIONS.containsKey(player.getUUID());
    }

    public static boolean isGroundResourceReserved(Level level, BlockPos pos) {
        return GROUND_RESERVATIONS.containsKey(GroundReservationKey.of(level, pos));
    }

    /**
     * Starts an operation where both participants are in the player's hands.
     * The consumed input is immediately reserved by removing it from the held
     * stack. It is restored if the action is cancelled before completion.
     */
    public static boolean tryStartHeldPair(
            ServerPlayer player,
            OperationMatch match,
            InteractionHand inputHand,
            InteractionHand toolHand
    ) {
        if (!canStart(player)) {
            return false;
        }

        OperationDefinition definition = match.definition();
        ItemStack inputStack = player.getItemInHand(inputHand);
        ItemStack toolStack = player.getItemInHand(toolHand);
        if (!definition.matches(inputStack, toolStack)) {
            return false;
        }

        ItemStack reservedInput = reserveHeldInput(player, inputStack, definition.inputCount());
        ActiveWorkAction action = new ActiveWorkAction(
                player.getUUID(),
                definition,
                player.level(),
                player.position(),
                toolHand,
                toolHand,
                ActiveWorkAction.ToolValidation.HELD_HAND,
                reservedInput,
                null,
                null,
                ActiveWorkAction.GroundRole.NONE
        );

        ACTIVE_ACTIONS.put(player.getUUID(), action);
        showStarted(player, action);
        return true;
    }

    /**
     * Starts an operation where one participant is a Ground Resource and the
     * other is held by the player. The ground object remains visible during
     * work and is logically reserved so it cannot be right-click-collected by
     * another player while the action is active.
     */
    public static boolean tryStartGroundPair(
            ServerPlayer player,
            OperationMatch match,
            InteractionHand heldHand,
            BlockPos groundPos,
            Item groundItem,
            boolean inputIsGround
    ) {
        if (!canStart(player)) {
            return false;
        }

        GroundReservationKey reservationKey = GroundReservationKey.of(player.level(), groundPos);
        if (GROUND_RESERVATIONS.containsKey(reservationKey)) {
            player.sendOverlayMessage(Component.translatable("message.realisticciv.work_target_reserved"));
            return false;
        }

        OperationDefinition definition = match.definition();
        ItemStack heldStack = player.getItemInHand(heldHand);
        ItemStack groundStack = new ItemStack(groundItem);

        ItemStack inputStack = inputIsGround ? groundStack : heldStack;
        ItemStack toolStack = inputIsGround ? heldStack : groundStack;
        if (!definition.matches(inputStack, toolStack)) {
            return false;
        }

        ItemStack reservedInput = ItemStack.EMPTY;
        if (!inputIsGround) {
            reservedInput = reserveHeldInput(player, heldStack, definition.inputCount());
        }

        InteractionHand heldToolHand = inputIsGround ? heldHand : null;
        ActiveWorkAction.GroundRole groundRole = inputIsGround
                ? ActiveWorkAction.GroundRole.INPUT
                : ActiveWorkAction.GroundRole.TOOL;

        ActiveWorkAction.ToolValidation toolValidation = inputIsGround
                ? ActiveWorkAction.ToolValidation.HELD_HAND
                : ActiveWorkAction.ToolValidation.GROUND_RESOURCE;

        ActiveWorkAction action = new ActiveWorkAction(
                player.getUUID(),
                definition,
                player.level(),
                player.position(),
                heldHand,
                heldToolHand,
                toolValidation,
                reservedInput,
                groundPos,
                groundItem,
                groundRole
        );

        GROUND_RESERVATIONS.put(reservationKey, player.getUUID());
        ACTIVE_ACTIONS.put(player.getUUID(), action);
        showStarted(player, action);
        return true;
    }

    /**
     * Starts a Hand Crafting operation after the 2x2 grid has already been
     * resolved and the consumed input has been reserved by the server.
     *
     * <p>The persistent tool is allowed to move from the 2x2 grid back into
     * normal inventory when the inventory screen closes. During the action we
     * therefore validate the required capability anywhere in the player's
     * inventory rather than pinning it to one hand.</p>
     */
    public static boolean tryStartHandCrafting(
            ServerPlayer player,
            OperationDefinition definition,
            ItemStack reservedInput
    ) {
        if (!canStart(player)) {
            return false;
        }
        if (definition.category() != OperationCategory.HAND_CRAFTING) {
            return false;
        }

        ActiveWorkAction action = new ActiveWorkAction(
                player.getUUID(),
                definition,
                player.level(),
                player.position(),
                InteractionHand.MAIN_HAND,
                null,
                ActiveWorkAction.ToolValidation.INVENTORY_CAPABILITY,
                reservedInput,
                null,
                null,
                ActiveWorkAction.GroundRole.NONE
        );

        ACTIVE_ACTIONS.put(player.getUUID(), action);
        showStarted(player, action);
        return true;
    }

    private static boolean canStart(ServerPlayer player) {
        if (ACTIVE_ACTIONS.containsKey(player.getUUID())) {
            player.sendOverlayMessage(Component.translatable("message.realisticciv.work_already_active"));
            return false;
        }
        return true;
    }

    private static ItemStack reserveHeldInput(ServerPlayer player, ItemStack inputStack, int count) {
        if (player.isCreative()) {
            return ItemStack.EMPTY;
        }

        ItemStack reserved = inputStack.copyWithCount(count);
        inputStack.shrink(count);
        return reserved;
    }

    private static void cancelAllForShutdown(MinecraftServer server) {
        for (ActiveWorkAction action : java.util.List.copyOf(ACTIVE_ACTIONS.values())) {
            ServerPlayer player = server.getPlayerList().getPlayer(action.playerId());
            if (player != null) {
                restoreReservedInput(player, action);
            }
            releaseGroundReservation(action);
        }
        ACTIVE_ACTIONS.clear();
        GROUND_RESERVATIONS.clear();
    }

    private static void tickServer(MinecraftServer server) {
        if (ACTIVE_ACTIONS.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<UUID, ActiveWorkAction>> iterator = ACTIVE_ACTIONS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, ActiveWorkAction> entry = iterator.next();
            ActiveWorkAction action = entry.getValue();
            ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());

            if (player == null) {
                releaseGroundReservation(action);
                iterator.remove();
                continue;
            }

            if (!isStillValid(player, action)) {
                cancelInternal(player, action, iterator, true);
                continue;
            }

            action.advance();

            if (shouldShowProgress(action)) {
                showProgress(player, action);
            }

            if (shouldPlayWorkPulse(action)) {
                playWorkPulse(player, action);
            }

            if (action.isComplete()) {
                complete(player, action);
                releaseGroundReservation(action);
                iterator.remove();
            }
        }
    }

    private static boolean isStillValid(ServerPlayer player, ActiveWorkAction action) {
        if (!player.isAlive()) {
            return false;
        }
        if (player.level() != action.level()) {
            return false;
        }
        if (player.position().distanceToSqr(action.startPosition()) > MAX_STATIONARY_DISTANCE_SQR) {
            return false;
        }

        switch (action.toolValidation()) {
            case HELD_HAND -> {
                if (action.heldToolHand() == null
                        || !player.getItemInHand(action.heldToolHand()).is(action.definition().toolTag())) {
                    return false;
                }
            }
            case INVENTORY_CAPABILITY -> {
                if (!player.getInventory().contains(action.definition().toolTag())) {
                    return false;
                }
            }
            case GROUND_RESOURCE, NONE -> {
                // Ground-resource validity is checked below. NONE has no
                // additional tool-location constraint.
            }
        }

        return validateGroundParticipant(action);
    }

    private static boolean validateGroundParticipant(ActiveWorkAction action) {
        if (action.groundRole() == ActiveWorkAction.GroundRole.NONE) {
            return true;
        }

        BlockState state = action.level().getBlockState(action.groundPos());
        if (!(state.getBlock() instanceof GroundResourceBlock groundResource)) {
            return false;
        }
        return groundResource.pickupItem() == action.expectedGroundItem();
    }

    private static boolean shouldShowProgress(ActiveWorkAction action) {
        return action.elapsedTicks() == 1
                || action.elapsedTicks() % PROGRESS_UPDATE_INTERVAL_TICKS == 0
                || action.isComplete();
    }

    private static void showStarted(ServerPlayer player, ActiveWorkAction action) {
        player.sendOverlayMessage(Component.translatable(
                "message.realisticciv.work_started",
                operationName(action.definition())
        ));
    }

    private static void showProgress(ServerPlayer player, ActiveWorkAction action) {
        int filled = Math.min(PROGRESS_SEGMENTS, Math.round(action.progress() * PROGRESS_SEGMENTS));
        String bar = "[" + "#".repeat(filled) + "-".repeat(PROGRESS_SEGMENTS - filled) + "]";
        int percent = Math.min(100, Math.round(action.progress() * 100.0F));

        player.sendOverlayMessage(Component.translatable(
                "message.realisticciv.work_progress",
                operationName(action.definition()),
                bar,
                percent + "%"
        ));
    }

    private static Component operationName(OperationDefinition definition) {
        return Component.translatable(definition.id().toLanguageKey("operation"));
    }

    private static boolean shouldPlayWorkPulse(ActiveWorkAction action) {
        int firstTick;
        int interval;
        if (action.definition().category() == OperationCategory.KNAPPING) {
            firstTick = KNAPPING_FIRST_STRIKE_TICK;
            interval = KNAPPING_STRIKE_INTERVAL_TICKS;
        } else if (action.definition().category() == OperationCategory.HAND_CRAFTING) {
            firstTick = 10;
            interval = 18;
        } else {
            return false;
        }

        if (action.elapsedTicks() < firstTick) {
            return false;
        }
        return (action.elapsedTicks() - firstTick) % interval == 0;
    }

    private static void playWorkPulse(ServerPlayer player, ActiveWorkAction action) {
        player.swing(action.animationHand());
        Vec3 effectPos = effectPosition(player, action);

        if (action.definition().category() == OperationCategory.KNAPPING) {
            float pitch = 1.05F + player.getRandom().nextFloat() * 0.35F;
            action.level().playSound(
                    null,
                    effectPos.x,
                    effectPos.y,
                    effectPos.z,
                    SoundEvents.STONE_HIT,
                    SoundSource.PLAYERS,
                    0.7F,
                    pitch
            );
            action.level().sendParticles(
                    new ItemParticleOption(ParticleTypes.ITEM, ModItems.FLINT_CHIPS),
                    effectPos.x,
                    effectPos.y,
                    effectPos.z,
                    4,
                    0.10D,
                    0.07D,
                    0.10D,
                    0.06D
            );
            return;
        }

        ItemStack particleStack = action.reservedInput().isEmpty()
                ? new ItemStack(ModItems.BRANCH)
                : action.reservedInput();
        action.level().playSound(
                null,
                effectPos.x,
                effectPos.y,
                effectPos.z,
                SoundEvents.WOOD_HIT,
                SoundSource.PLAYERS,
                0.55F,
                1.10F + player.getRandom().nextFloat() * 0.20F
        );
        action.level().sendParticles(
                new ItemParticleOption(ParticleTypes.ITEM, particleStack.getItem()),
                effectPos.x,
                effectPos.y,
                effectPos.z,
                2,
                0.06D,
                0.05D,
                0.06D,
                0.03D
        );
    }

    private static Vec3 effectPosition(ServerPlayer player, ActiveWorkAction action) {
        if (action.groundPos() != null) {
            BlockPos pos = action.groundPos();
            return new Vec3(pos.getX() + 0.5D, pos.getY() + 0.16D, pos.getZ() + 0.5D);
        }

        Vec3 eye = player.getEyePosition();
        return eye.add(player.getLookAngle().scale(0.70D)).add(0.0D, -0.55D, 0.0D);
    }

    private static void complete(ServerPlayer player, ActiveWorkAction action) {
        if (action.groundRole() == ActiveWorkAction.GroundRole.INPUT
                && validateGroundParticipant(action)) {
            action.level().removeBlock(action.groundPos(), false);
        }

        for (OperationOutputDefinition output : action.definition().outputs()) {
            ItemStack result = output.createStack();
            if (!player.addItem(result)) {
                player.drop(result, false);
            }
        }

        Vec3 effectPos = effectPosition(player, action);
        if (action.definition().category() == OperationCategory.KNAPPING) {
            action.level().playSound(
                    null,
                    effectPos.x,
                    effectPos.y,
                    effectPos.z,
                    SoundEvents.STONE_BREAK,
                    SoundSource.PLAYERS,
                    0.9F,
                    1.15F + player.getRandom().nextFloat() * 0.20F
            );
            action.level().sendParticles(
                    new ItemParticleOption(ParticleTypes.ITEM, ModItems.FLINT_CHIPS),
                    effectPos.x,
                    effectPos.y,
                    effectPos.z,
                    8,
                    0.13D,
                    0.10D,
                    0.13D,
                    0.09D
            );
        } else if (action.definition().category() == OperationCategory.HAND_CRAFTING) {
            ItemStack particleStack = action.reservedInput().isEmpty()
                    ? new ItemStack(ModItems.BRANCH)
                    : action.reservedInput();
            action.level().playSound(
                    null,
                    effectPos.x,
                    effectPos.y,
                    effectPos.z,
                    SoundEvents.WOOD_BREAK,
                    SoundSource.PLAYERS,
                    0.75F,
                    1.05F + player.getRandom().nextFloat() * 0.15F
            );
            action.level().sendParticles(
                    new ItemParticleOption(ParticleTypes.ITEM, particleStack.getItem()),
                    effectPos.x,
                    effectPos.y,
                    effectPos.z,
                    5,
                    0.09D,
                    0.07D,
                    0.09D,
                    0.05D
            );
        }

        player.sendOverlayMessage(Component.translatable(
                "message.realisticciv.work_complete",
                operationName(action.definition())
        ));
    }

    private static void cancel(ServerPlayer player, boolean silent, boolean restoreInput) {
        ActiveWorkAction action = ACTIVE_ACTIONS.remove(player.getUUID());
        if (action == null) {
            return;
        }

        if (restoreInput) {
            restoreReservedInput(player, action);
        }
        releaseGroundReservation(action);
        if (!silent) {
            player.sendOverlayMessage(Component.translatable("message.realisticciv.work_cancelled"));
        }
    }

    private static void cancelInternal(
            ServerPlayer player,
            ActiveWorkAction action,
            Iterator<Map.Entry<UUID, ActiveWorkAction>> iterator,
            boolean restoreInput
    ) {
        if (restoreInput) {
            restoreReservedInput(player, action);
        }
        releaseGroundReservation(action);
        iterator.remove();
        player.sendOverlayMessage(Component.translatable("message.realisticciv.work_cancelled"));
    }

    private static void restoreReservedInput(ServerPlayer player, ActiveWorkAction action) {
        if (action.reservedInput().isEmpty()) {
            return;
        }

        ItemStack restored = action.reservedInput().copy();
        if (!player.addItem(restored)) {
            player.drop(restored, false);
        }
    }

    private static void releaseGroundReservation(ActiveWorkAction action) {
        if (action.groundPos() == null) {
            return;
        }
        GROUND_RESERVATIONS.remove(GroundReservationKey.of(action.level(), action.groundPos()));
    }

    private record GroundReservationKey(net.minecraft.resources.ResourceKey<Level> dimension, BlockPos pos) {
        private GroundReservationKey {
            pos = pos.immutable();
        }

        static GroundReservationKey of(Level level, BlockPos pos) {
            return new GroundReservationKey(level.dimension(), pos);
        }
    }
}
