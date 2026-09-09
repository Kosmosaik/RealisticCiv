package com.realisticciv.world;

import com.realisticciv.RealisticCiv;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/** Registry keys for JSON-defined surface resource features. */
public final class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> GROUND_BRANCH = create("ground_branch");
    public static final ResourceKey<PlacedFeature> GROUND_GRANITE_STONE = create("ground_granite_stone");
    public static final ResourceKey<PlacedFeature> GROUND_FLINT_NODULE = create("ground_flint_nodule");

    private ModPlacedFeatures() {
    }

    private static ResourceKey<PlacedFeature> create(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, RealisticCiv.id(path));
    }
}
