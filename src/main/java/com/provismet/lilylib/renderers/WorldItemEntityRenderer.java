/*
 * Copyright (C) 2024 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.renderers;

import com.provismet.lilylib.interfaces.entity.WorldItemEntity;
import com.provismet.lilylib.renderers.states.WorldItemEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;

/**
 * EntityRenderer for {@link WorldItemEntity}.
 * <p> This renders items in the world similar to how dropped items work in vanilla.
 * <p> The rotation and offset within the bounding box of the entity are controlled by the implementing class.
 */
@Environment(EnvType.CLIENT)
public class WorldItemEntityRenderer<T extends Entity> extends EntityRenderer<T, WorldItemEntityRenderState> {
    private final ItemModelManager modelManager;

    public WorldItemEntityRenderer (Context ctx) {
        super(ctx);
        this.modelManager = ctx.getItemModelManager();
    }

    @Override
    public WorldItemEntityRenderState createRenderState () {
        return new WorldItemEntityRenderState();
    }

    @Override
    public void updateRenderState (T entity, WorldItemEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        if (entity instanceof WorldItemEntity worldItem) {
            state.xRotation = worldItem.getXRotation(tickDelta);
            state.yRotation = worldItem.getYRotation(tickDelta);
            state.zRotation = worldItem.getZRotation(tickDelta);
            state.xOffset = worldItem.getXOffset(tickDelta);
            state.yOffset = worldItem.getYOffset(tickDelta);
            state.zOffset = worldItem.getZOffset(tickDelta);
            state.update(entity, worldItem.getStack(), this.modelManager);
        }
    }

    @Override
    public void render (WorldItemEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();
        matrices.translate(state.xOffset, state.yOffset, state.zOffset);
        if (state.xRotation != 0) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(state.xRotation));
        if (state.yRotation != 0) matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yRotation));
        if (state.zRotation != 0) matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.zRotation));

        state.itemRenderState.render(matrices, queue, state.light, OverlayTexture.DEFAULT_UV, state.outlineColor);
        matrices.pop();

        super.render(state, matrices, queue, cameraState);
    }
}
