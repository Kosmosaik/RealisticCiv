package com.realisticciv.content.block;

import com.realisticciv.content.block.entity.GroundResourceBlockEntity;
import com.realisticciv.crafting.knapping.PrimitiveKnappingService;
import com.realisticciv.crafting.workaction.WorkActionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.network.chat.Component;

import java.util.Objects;

/**
 * Small, non-colliding world anchor representing a naturally loose resource.
 *
 * <p>The block itself no longer owns a bespoke visible model. On the client a
 * lightweight block-entity renderer draws the canonical inventory item model
 * directly at this position. That means the same Branch / Granite Stone /
 * Flint Nodule texture is used in inventory and in the world, including the
 * slight pixel-extrusion thickness Minecraft gives generated item models.</p>
 *
 * <p>Pickup mutation happens only on the logical server. The client merely
 * reports a successful interaction and receives the authoritative inventory /
 * block updates from the server.</p>
 */
public final class GroundResourceBlock extends Block implements EntityBlock {
    private final Item pickupItem;
    private final VoxelShape outlineShape;
    private final float worldRenderScale;
    private final float worldRenderYOffset;

    public GroundResourceBlock(
            Properties properties,
            Item pickupItem,
            VoxelShape outlineShape,
            float worldRenderScale,
            float worldRenderYOffset
    ) {
        super(properties);
        this.pickupItem = Objects.requireNonNull(pickupItem, "pickupItem");
        this.outlineShape = Objects.requireNonNull(outlineShape, "outlineShape");
        this.worldRenderScale = worldRenderScale;
        this.worldRenderYOffset = worldRenderYOffset;
    }

    public Item pickupItem() {
        return pickupItem;
    }

    public float worldRenderScale() {
        return worldRenderScale;
    }

    public float worldRenderYOffset() {
        return worldRenderYOffset;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GroundResourceBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        return collect(level, pos, player);
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        if (!level.isClientSide() && WorkActionManager.isGroundResourceReserved(level, pos)) {
            player.sendOverlayMessage(Component.translatable("message.realisticciv.work_target_reserved"));
            return InteractionResult.SUCCESS;
        }

        InteractionResult knapping = PrimitiveKnappingService.tryKnapGroundPair(
                level,
                pos,
                player,
                hand,
                stack,
                pickupItem
        );
        if (knapping != InteractionResult.PASS) {
            return knapping;
        }

        return collect(level, pos, player);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return outlineShape;
    }

    private InteractionResult collect(Level level, BlockPos pos, Player player) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer && WorkActionManager.isWorking(serverPlayer)) {
            player.sendOverlayMessage(Component.translatable("message.realisticciv.work_already_active"));
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide() && WorkActionManager.isGroundResourceReserved(level, pos)) {
            player.sendOverlayMessage(Component.translatable("message.realisticciv.work_target_reserved"));
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide()) {
            ItemStack collected = new ItemStack(pickupItem);
            if (!player.addItem(collected)) {
                player.drop(collected, false);
            }
            level.removeBlock(pos, false);
        }

        return InteractionResult.SUCCESS;
    }
}
