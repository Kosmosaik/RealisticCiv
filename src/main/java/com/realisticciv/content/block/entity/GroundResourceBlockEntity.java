package com.realisticciv.content.block.entity;

import com.realisticciv.content.block.GroundResourceBlock;
import com.realisticciv.registry.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Stateless render anchor for a Ground Resource.
 *
 * <p>The canonical item is derived from the owning {@link GroundResourceBlock};
 * it is not duplicated into block-entity NBT. This entity exists so the client
 * can render the inventory item model in-world while the server continues to
 * treat the resource as a persistent, server-authoritative block.</p>
 */
public final class GroundResourceBlockEntity extends BlockEntity {
    public GroundResourceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.GROUND_RESOURCE, pos, state);
    }

    public ItemStack pickupStack() {
        if (getBlockState().getBlock() instanceof GroundResourceBlock resourceBlock) {
            return new ItemStack(resourceBlock.pickupItem());
        }
        return ItemStack.EMPTY;
    }

    public float worldRenderScale() {
        if (getBlockState().getBlock() instanceof GroundResourceBlock resourceBlock) {
            return resourceBlock.worldRenderScale();
        }
        return 1.0F;
    }

    public float worldRenderYOffset() {
        if (getBlockState().getBlock() instanceof GroundResourceBlock resourceBlock) {
            return resourceBlock.worldRenderYOffset();
        }
        return 0.03125F;
    }
}
