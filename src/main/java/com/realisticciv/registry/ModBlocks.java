package com.realisticciv.registry;

import com.realisticciv.content.block.GroundResourceBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

/**
 * World-block registrations.
 *
 * <p>Ground resource blocks deliberately have no BlockItem. Their inventory
 * representation is the canonical material item from {@link ModItems}. The
 * world renderer reuses that same item model instead of maintaining duplicate
 * block art.</p>
 */
public final class ModBlocks {
    // Shapes stay centered on the block because the item renderer is centered
    // too. They are intentionally a little generous for comfortable pickup.
    private static final VoxelShape BRANCH_SHAPE = Shapes.box(0.0625, 0.0, 0.125, 0.9375, 0.1875, 0.875);
    private static final VoxelShape STONE_SHAPE = Shapes.box(0.1875, 0.0, 0.1875, 0.8125, 0.3125, 0.8125);

    public static final Block GROUND_BRANCH = registerGroundResource(
            ModBlockKeys.GROUND_BRANCH,
            ModItems.BRANCH,
            BRANCH_SHAPE,
            SoundType.WOOD,
            1.20F,
            0.040F
    );

    public static final Block GROUND_GRANITE_STONE = registerGroundResource(
            ModBlockKeys.GROUND_GRANITE_STONE,
            ModItems.GRANITE_STONE,
            STONE_SHAPE,
            SoundType.STONE,
            0.8F,
            0.045F
    );

    public static final Block GROUND_FLINT_NODULE = registerGroundResource(
            ModBlockKeys.GROUND_FLINT_NODULE,
            ModItems.FLINT_NODULE,
            STONE_SHAPE,
            SoundType.STONE,
            0.8F,
            0.045F
    );

    private ModBlocks() {
    }

    public static void initialize() {
        // Static field initialization performs registration.
    }

    private static Block registerGroundResource(
            ResourceKey<Block> key,
            Item pickupItem,
            VoxelShape shape,
            SoundType sound,
            float worldRenderScale,
            float worldRenderYOffset
    ) {
        return register(
                key,
                properties -> new GroundResourceBlock(
                        properties,
                        pickupItem,
                        shape,
                        worldRenderScale,
                        worldRenderYOffset
                ),
                BlockBehaviour.Properties.of()
                        .noCollision()
                        .noOcclusion()
                        .replaceable()
                        .instabreak()
                        .sound(sound)
        );
    }

    private static Block register(
            ResourceKey<Block> key,
            Function<BlockBehaviour.Properties, Block> factory,
            BlockBehaviour.Properties properties
    ) {
        Block block = factory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }
}
