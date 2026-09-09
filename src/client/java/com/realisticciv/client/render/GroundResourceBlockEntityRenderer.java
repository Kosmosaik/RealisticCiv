package com.realisticciv.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.realisticciv.content.block.entity.GroundResourceBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

/**
 * Renders a persistent Ground Resource using its canonical inventory item model.
 *
 * <p>This deliberately mirrors Minecraft's generated-item geometry rather than
 * maintaining a second hand-authored block model. The transparent pixels keep
 * the item's silhouette, while Minecraft's generated model provides the subtle
 * extrusion/thickness requested for items lying on the ground.</p>
 */
public final class GroundResourceBlockEntityRenderer
        implements BlockEntityRenderer<GroundResourceBlockEntity, GroundResourceRenderState> {

    private final ItemModelResolver itemModelResolver;

    public GroundResourceBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public GroundResourceRenderState createRenderState() {
        return new GroundResourceRenderState();
    }

    @Override
    public void extractRenderState(
            GroundResourceBlockEntity blockEntity,
            GroundResourceRenderState renderState,
            float partialTick,
            Vec3 cameraPosition,
            ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
    ) {
        BlockEntityRenderer.super.extractRenderState(
                blockEntity,
                renderState,
                partialTick,
                cameraPosition,
                crumblingOverlay
        );

        BlockPos pos = blockEntity.getBlockPos();
        int seed = stableSeed(pos);

        itemModelResolver.updateForTopItem(
                renderState.item,
                blockEntity.pickupStack(),
                ItemDisplayContext.FIXED,
                blockEntity.getLevel(),
                null,
                seed
        );

        renderState.yawDegrees = ((seed >>> 8) & 0xFFFF) * (360.0F / 65536.0F);
        renderState.scale = blockEntity.worldRenderScale();
        renderState.yOffset = blockEntity.worldRenderYOffset();
    }

    @Override
    public void submit(
            GroundResourceRenderState renderState,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState cameraRenderState
    ) {
        if (renderState.item.isEmpty()) {
            return;
        }

        poseStack.pushPose();

        // Center the visual on the same block position as the interaction shape.
        poseStack.translate(0.5F, renderState.yOffset, 0.5F);

        // Stable per-position yaw keeps natural scatter without changing the
        // block's interaction coordinates or requiring extra block states/NBT.
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yawDegrees));

        // Generated item models are upright. Lay the item's thin extruded sprite
        // horizontally so it rests on the terrain rather than bobbing/spinning.
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(renderState.scale, renderState.scale, renderState.scale);

        renderState.item.submit(
                poseStack,
                submitNodeCollector,
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();
    }

    private static int stableSeed(BlockPos pos) {
        long value = pos.asLong();
        value ^= value >>> 33;
        value *= 0xff51afd7ed558ccdL;
        value ^= value >>> 33;
        value *= 0xc4ceb9fe1a85ec53L;
        value ^= value >>> 33;
        return (int) (value ^ (value >>> 32));
    }
}
