package com.realisticciv;

import com.realisticciv.crafting.operation.OperationCatalog;
import com.realisticciv.crafting.workaction.WorkActionManager;
import com.realisticciv.material.MaterialCatalog;
import com.realisticciv.networking.ModNetworking;
import com.realisticciv.progression.VanillaBootstrapLocks;
import com.realisticciv.registry.ModBlockEntityTypes;
import com.realisticciv.registry.ModBlocks;
import com.realisticciv.registry.ModCreativeTabs;
import com.realisticciv.registry.ModItems;
import com.realisticciv.world.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RealisticCiv implements ModInitializer {
    public static final String MOD_ID = "realisticciv";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing RealisticCiv");

        ModItems.initialize();
        ModBlocks.initialize();
        ModBlockEntityTypes.initialize();
        ModCreativeTabs.initialize();
        MaterialCatalog.initialize();
        OperationCatalog.initialize();
        WorkActionManager.initialize();
        ModNetworking.initialize();
        ModWorldGeneration.initialize();
        VanillaBootstrapLocks.initialize();

        LOGGER.info("RealisticCiv v0.1.5 Hand Crafting foundation initialized");
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
