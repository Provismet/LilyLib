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
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.RotationAxis;

/**
 * EntityRenderer for {@link WorldItemEntity}.
 * <p> This renders items in the world similar to how dropped items work in vanilla.
 * <p> The rotation and offset within the bounding box of the entity are controlled by the implementing class.
 */
@Environment(EnvType.CLIENT)
public class WorldItemEntityRenderer<T extends Entity> extends EntityRenderer<T, WorldItemEntityRenderState> {
    private final ItemRenderer itemRenderer;

    public WorldItemEntityRenderer (Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
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
            state.stack = worldItem.getStack().copy();
            state.model = this.itemRenderer.getModel(worldItem.getStack(), entity.getWorld(), null, entity.getId());
        }
    }

    @Override
    public void render (WorldItemEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(state.xOffset, state.yOffset, state.zOffset);
        if (state.xRotation != 0) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(state.xRotation));
        if (state.yRotation != 0) matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yRotation));
        if (state.zRotation != 0) matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.zRotation));

        this.itemRenderer.renderItem(state.stack, ModelTransformationMode.GROUND, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, state.model);
        matrices.pop();

        super.render(state, matrices, vertexConsumers, light);
    }
}
