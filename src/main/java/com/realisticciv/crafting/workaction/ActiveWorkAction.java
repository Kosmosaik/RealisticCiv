package com.realisticciv.crafting.workaction;

import com.realisticciv.crafting.operation.OperationDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.UUID;

/**
 * Runtime-only state for one active piece of player work.
 *
 * <p>This is deliberately not persistent save data. A WorkAction is short-lived
 * server simulation state: inputs are reserved when the action begins, the
 * server advances progress each tick, then either completes the operation or
 * restores/releases reservations on cancellation.</p>
 */
final class ActiveWorkAction {
    enum GroundRole {
        NONE,
        INPUT,
        TOOL
    }

    enum ToolValidation {
        HELD_HAND,
        INVENTORY_CAPABILITY,
        GROUND_RESOURCE,
        NONE
    }

    private final UUID playerId;
    private final OperationDefinition definition;
    private final ServerLevel level;
    private final Vec3 startPosition;
    private final InteractionHand animationHand;
    private final InteractionHand heldToolHand;
    private final ToolValidation toolValidation;
    private final ItemStack reservedInput;
    private final BlockPos groundPos;
    private final Item expectedGroundItem;
    private final GroundRole groundRole;
    private int elapsedTicks;

    ActiveWorkAction(
            UUID playerId,
            OperationDefinition definition,
            ServerLevel level,
            Vec3 startPosition,
            InteractionHand animationHand,
            InteractionHand heldToolHand,
            ToolValidation toolValidation,
            ItemStack reservedInput,
            BlockPos groundPos,
            Item expectedGroundItem,
            GroundRole groundRole
    ) {
        this.playerId = Objects.requireNonNull(playerId, "playerId");
        this.definition = Objects.requireNonNull(definition, "definition");
        this.level = Objects.requireNonNull(level, "level");
        this.startPosition = Objects.requireNonNull(startPosition, "startPosition");
        this.animationHand = Objects.requireNonNull(animationHand, "animationHand");
        this.heldToolHand = heldToolHand;
        this.toolValidation = Objects.requireNonNull(toolValidation, "toolValidation");
        this.reservedInput = Objects.requireNonNull(reservedInput, "reservedInput");
        this.groundPos = groundPos == null ? null : groundPos.immutable();
        this.expectedGroundItem = expectedGroundItem;
        this.groundRole = Objects.requireNonNull(groundRole, "groundRole");
    }

    UUID playerId() {
        return playerId;
    }

    OperationDefinition definition() {
        return definition;
    }

    ServerLevel level() {
        return level;
    }

    Vec3 startPosition() {
        return startPosition;
    }

    InteractionHand animationHand() {
        return animationHand;
    }

    InteractionHand heldToolHand() {
        return heldToolHand;
    }

    ToolValidation toolValidation() {
        return toolValidation;
    }

    ItemStack reservedInput() {
        return reservedInput;
    }

    BlockPos groundPos() {
        return groundPos;
    }

    Item expectedGroundItem() {
        return expectedGroundItem;
    }

    GroundRole groundRole() {
        return groundRole;
    }

    int elapsedTicks() {
        return elapsedTicks;
    }

    int durationTicks() {
        return Math.max(1, definition.baseDurationTicks());
    }

    void advance() {
        elapsedTicks++;
    }

    boolean isComplete() {
        return elapsedTicks >= durationTicks();
    }

    float progress() {
        return Math.min(1.0F, elapsedTicks / (float) durationTicks());
    }
}
