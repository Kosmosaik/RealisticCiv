package com.realisticciv.networking;

import com.realisticciv.RealisticCiv;
import com.realisticciv.crafting.handcrafting.HandCraftingService;
import com.realisticciv.networking.payload.StartHandCraftPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

/** RealisticCiv play-network payload registration. */
public final class ModNetworking {
    private static boolean initialized;

    private ModNetworking() {
    }

    public static void initialize() {
        if (initialized) {
            return;
        }

        PayloadTypeRegistry.serverboundPlay().register(StartHandCraftPayload.TYPE, StartHandCraftPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(
                StartHandCraftPayload.TYPE,
                (payload, context) -> HandCraftingService.tryStartFromInventoryGrid(context.player())
        );

        initialized = true;
        RealisticCiv.LOGGER.info("Initialized RealisticCiv networking");
    }
}
