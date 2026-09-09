package com.realisticciv.world;

import com.realisticciv.registry.ModBiomeTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.levelgen.GenerationStep;

/** Injects RealisticCiv's JSON-defined loose-resource features into biomes. */
public final class ModWorldGeneration {
    private ModWorldGeneration() {
    }

    public static void initialize() {
        // Fallen branches use a RealisticCiv-owned biome tag instead of only
        // minecraft:is_forest. Vanilla taigas/jungles are forested environments
        // too, and modpacks can extend this tag for their own wooded biomes.
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModBiomeTags.BRANCH_RICH),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.GROUND_BRANCH
        );

        // Stones use support predicates in their placed-feature JSON to avoid
        // unsuitable surfaces. The biome selector only limits them to Overworld.
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.GROUND_GRANITE_STONE
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.GROUND_FLINT_NODULE
        );
    }
}
