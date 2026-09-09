package com.realisticciv.client.datagen;

import com.realisticciv.registry.ModItemKeys;
import com.realisticciv.registry.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public final class RealisticCivItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public RealisticCivItemTagProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registryLookup) {
        builder(ModItemTags.NATURAL_BRANCHES)
                .add(ModItemKeys.BRANCH);

        builder(ModItemTags.LOOSE_STONES)
                .add(ModItemKeys.GRANITE_STONE)
                .add(ModItemKeys.FLINT_NODULE);

        builder(ModItemTags.HAMMERSTONES)
                .add(ModItemKeys.GRANITE_STONE);

        builder(ModItemTags.KNAPPABLE_STONES)
                .add(ModItemKeys.FLINT_NODULE)
                .add(ModItemKeys.FLINT_CORE);

        builder(ModItemTags.KNAPPING_NODULES)
                .add(ModItemKeys.FLINT_NODULE);

        builder(ModItemTags.KNAPPING_CORES)
                .add(ModItemKeys.FLINT_CORE);

        builder(ModItemTags.STONE_FLAKES)
                .add(ModItemKeys.FLINT_FLAKE);

        builder(ModItemTags.KNAPPING_WASTE)
                .add(ModItemKeys.FLINT_CHIPS);

        // A fresh flint flake is already a usable primitive cutting edge. Later
        // knives/retouched flakes can join the same capability tag.
        builder(ModItemTags.CUTTING_EDGES)
                .add(ModItemKeys.FLINT_FLAKE);

        builder(ModItemTags.TOOL_HAFTS)
                .add(ModItemKeys.WOODEN_HAFT);

        // Intentionally empty until RealisticCiv adds its first legitimate axe.
        builder(ModItemTags.FELLING_TOOLS);
    }
}
