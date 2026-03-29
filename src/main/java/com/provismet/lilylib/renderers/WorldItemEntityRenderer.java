/*
 * Copyright (C) 2024-2026 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.provismet.lilylib.interfaces.entity.WorldItemEntity;
import com.provismet.lilylib.renderers.states.WorldItemEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;

/**
 * EntityRenderer for {@link WorldItemEntity}.
 * <p> This renders items in the world similar to how dropped items work in vanilla.
 * <p> The rotation and offset within the bounding box of the entity are controlled by the implementing class.
 */
@Environment(EnvType.CLIENT)
public class WorldItemEntityRenderer<T extends Entity> extends EntityRenderer<T, WorldItemEntityRenderState> {
    private final ItemModelResolver modelManager;

    public WorldItemEntityRenderer (Context ctx) {
        super(ctx);
        this.modelManager = ctx.getItemModelResolver();
    }

    @Override
    public WorldItemEntityRenderState createRenderState () {
        return new WorldItemEntityRenderState();
    }

    @Override
    public void extractRenderState (T entity, WorldItemEntityRenderState state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        if (entity instanceof WorldItemEntity worldItem) {
            state.xRotation = worldItem.getXRotation(tickDelta);
            state.yRotation = worldItem.getYRotation(tickDelta);
            state.zRotation = worldItem.getZRotation(tickDelta);
            state.xOffset = worldItem.getXOffset(tickDelta);
            state.yOffset = worldItem.getYOffset(tickDelta);
            state.zOffset = worldItem.getZOffset(tickDelta);
            state.extractItemGroupRenderState(entity, worldItem.getItem(), this.modelManager);
        }
    }

    @Override
    public void submit (WorldItemEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        matrices.pushPose();
        matrices.translate(state.xOffset, state.yOffset, state.zOffset);
        if (state.xRotation != 0) matrices.mulPose(Axis.XP.rotationDegrees(state.xRotation));
        if (state.yRotation != 0) matrices.mulPose(Axis.YP.rotationDegrees(state.yRotation));
        if (state.zRotation != 0) matrices.mulPose(Axis.ZP.rotationDegrees(state.zRotation));

        state.item.submit(matrices, queue, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        matrices.popPose();

        super.submit(state, matrices, queue, cameraState);
    }
}
