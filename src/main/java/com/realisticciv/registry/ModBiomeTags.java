package com.realisticciv.registry;

import com.realisticciv.RealisticCiv;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/**
 * Biome categories owned by RealisticCiv so world-resource placement can be
 * extended by data packs/modpacks without changing Java code.
 */
public final class ModBiomeTags {
    /** Biomes expected to accumulate abundant naturally fallen woody debris. */
    public static final TagKey<Biome> BRANCH_RICH = create("branch_rich");

    private ModBiomeTags() {
    }

    private static TagKey<Biome> create(String path) {
        return TagKey.create(Registries.BIOME, RealisticCiv.id(path));
    }
}
