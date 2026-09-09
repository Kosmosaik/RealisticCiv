package com.realisticciv.client.datagen;

import com.realisticciv.registry.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public final class RealisticCivModelProvider extends FabricModelProvider {
    public RealisticCivModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        // Ground Resources are rendered by a block-entity renderer using the
        // canonical item model, so no visible block model is generated here.
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModItems.BRANCH, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.GRANITE_STONE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FLINT_NODULE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FLINT_CORE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FLINT_FLAKE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FLINT_CHIPS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WOODEN_HAFT, ModelTemplates.FLAT_HANDHELD_ITEM);
    }

    @Override
    public String getName() {
        return "RealisticCiv models";
    }
}
