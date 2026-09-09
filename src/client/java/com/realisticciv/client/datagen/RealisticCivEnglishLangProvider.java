package com.realisticciv.client.datagen;

import com.realisticciv.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public final class RealisticCivEnglishLangProvider extends FabricLanguageProvider {
    public RealisticCivEnglishLangProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registryLookup
    ) {
        super(output, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(
            HolderLookup.Provider registryLookup,
            TranslationBuilder translationBuilder
    ) {
        translationBuilder.add(ModItems.BRANCH, "Branch");
        translationBuilder.add(ModItems.GRANITE_STONE, "Granite Stone");
        translationBuilder.add(ModItems.FLINT_NODULE, "Flint Nodule");
        translationBuilder.add(ModItems.FLINT_CORE, "Flint Core");
        translationBuilder.add(ModItems.FLINT_FLAKE, "Flint Flake");
        translationBuilder.add(ModItems.FLINT_CHIPS, "Flint Chips");
        translationBuilder.add(ModItems.WOODEN_HAFT, "Wooden Handle");
        translationBuilder.add("creativeTab.realisticciv", "RealisticCiv");
        translationBuilder.add("message.realisticciv.requires_felling_tool", "You need a suitable felling tool to cut this tree.");
        translationBuilder.add("operation.realisticciv.initial_flint_knapping", "Knapping Flint");
        translationBuilder.add("operation.realisticciv.shape_wooden_haft", "Shape Wooden Handle");
        translationBuilder.add("message.realisticciv.work_started", "%s started.");
        translationBuilder.add("message.realisticciv.work_progress", "%s  %s  %s");
        translationBuilder.add("message.realisticciv.work_complete", "%s complete.");
        translationBuilder.add("message.realisticciv.work_cancelled", "Work cancelled.");
        translationBuilder.add("message.realisticciv.work_already_active", "You are already working.");
        translationBuilder.add("message.realisticciv.work_target_reserved", "That resource is currently being worked.");
        translationBuilder.add("message.realisticciv.knapping_complete", "You strike the flint and produce usable flakes.");
        translationBuilder.add("message.realisticciv.hand_crafting_inventory_only", "Hand Crafting can only be started from your inventory.");
        translationBuilder.add("message.realisticciv.hand_crafting_no_operation", "Those items do not form a known Hand Crafting operation.");
        translationBuilder.add("message.realisticciv.hand_crafting_changed", "The Hand Crafting inputs changed before work could begin.");
        translationBuilder.add("gui.realisticciv.hand_crafting.craft", "Craft");
        translationBuilder.add("gui.realisticciv.hand_crafting.no_match", "Place a valid Hand Crafting material and tool in the 2x2 grid.");
        translationBuilder.add("gui.realisticciv.hand_crafting.ready", "%s\nTime: %s s\nInput is consumed; tool is retained.");
    }
}
