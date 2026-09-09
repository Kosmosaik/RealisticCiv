package com.realisticciv.client;

import com.realisticciv.client.render.GroundResourceBlockEntityRenderer;
import com.realisticciv.client.ui.HandCraftingInventoryUi;
import com.realisticciv.registry.ModBlockEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public final class RealisticCivClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandCraftingInventoryUi.initialize();

        BlockEntityRenderers.register(
                ModBlockEntityTypes.GROUND_RESOURCE,
                GroundResourceBlockEntityRenderer::new
        );
    }
}
