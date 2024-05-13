/*
 * Copyright (C) 2024 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.20/LICENSE for the full license.
 */

package com.provismet.lilylib.renderers;

import com.provismet.lilylib.interfaces.entity.WorldItemEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

/**
 * EntityRenderer for {@link WorldItemEntity}.
 * <p> This renders items in the world similar to how dropped items work in vanilla.
 * <p> The rotation and offset within the bounding box of the entity are controlled by the implementing class.
 */
@Environment(EnvType.CLIENT)
public class WorldItemEntityRenderer<T extends Entity> extends EntityRenderer<T> {
    private final ItemRenderer itemRenderer;

    public WorldItemEntityRenderer (Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render (T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        ItemStack itemStack;
        if (entity instanceof WorldItemEntity itemEntity) itemStack = itemEntity.getStack();
        else return;

        matrices.push();
        BakedModel model = this.itemRenderer.getModel(itemStack, entity.getWorld(), null, entity.getId());
        float rx = itemEntity.getXRotation(tickDelta);
        float ry = itemEntity.getYRotation(tickDelta);
        float rz = itemEntity.getZRotation(tickDelta);
        
        float dx = itemEntity.getXOffset(tickDelta);
        float dy = itemEntity.getYOffset(tickDelta);
        float dz = itemEntity.getZOffset(tickDelta);

        matrices.translate(dx, dy, dz);
        if (rx != 0) matrices.multiply(new Quaternion(Vec3f.POSITIVE_X, rx, true));
        if (ry != 0) matrices.multiply(new Quaternion(Vec3f.POSITIVE_Y, ry, true));
        if (rz != 0) matrices.multiply(new Quaternion(Vec3f.POSITIVE_Z, rz, true));
        this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, model);
        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    @SuppressWarnings("deprecation")
    public Identifier getTexture (T entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
