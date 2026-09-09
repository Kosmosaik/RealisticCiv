package com.realisticciv.registry;

import com.realisticciv.RealisticCiv;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

/** Stable registry keys for non-inventory RealisticCiv world blocks. */
public final class ModBlockKeys {
    public static final ResourceKey<Block> GROUND_BRANCH = create("ground_branch");
    public static final ResourceKey<Block> GROUND_GRANITE_STONE = create("ground_granite_stone");
    public static final ResourceKey<Block> GROUND_FLINT_NODULE = create("ground_flint_nodule");

    private ModBlockKeys() {
    }

    private static ResourceKey<Block> create(String path) {
        return ResourceKey.create(Registries.BLOCK, RealisticCiv.id(path));
    }
}
