package com.realisticciv.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

/** Extracted client render data for one Ground Resource. */
public final class GroundResourceRenderState extends BlockEntityRenderState {
    public final ItemStackRenderState item = new ItemStackRenderState();
    public float yawDegrees;
    public float scale = 1.0F;
    public float yOffset = 0.03125F;
}
