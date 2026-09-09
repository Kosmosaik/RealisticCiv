package com.realisticciv.registry;

import com.realisticciv.RealisticCiv;
import com.realisticciv.content.block.entity.GroundResourceBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

/** Block-entity registrations used by RealisticCiv world objects. */
public final class ModBlockEntityTypes {
    public static final BlockEntityType<GroundResourceBlockEntity> GROUND_RESOURCE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            RealisticCiv.id("ground_resource"),
            new BlockEntityType<>(
                    GroundResourceBlockEntity::new,
                    Set.of(
                            ModBlocks.GROUND_BRANCH,
                            ModBlocks.GROUND_GRANITE_STONE,
                            ModBlocks.GROUND_FLINT_NODULE
                    )
            )
    );

    private ModBlockEntityTypes() {
    }

    public static void initialize() {
        // Static field initialization performs registration.
    }
}
