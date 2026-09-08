package com.realisticciv;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RealisticCiv implements ModInitializer {
    public static final String MOD_ID = "realisticciv";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private RealisticCiv() {
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing RealisticCiv");
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
