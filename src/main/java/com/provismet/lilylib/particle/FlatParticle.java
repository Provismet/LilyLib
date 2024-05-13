/*
 * Copyright (C) 2024 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.20/LICENSE for the full license.
 */

package com.provismet.lilylib.particle;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

/**
 * <p> A particle that renders flat on the ground.
 * <p> Supports animated sprites.
 */
public abstract class FlatParticle extends SpriteBillboardParticle {
    protected final SpriteProvider spriteProvider;

    protected float angleX;
    protected float prevAngleX;
    protected float angleZ;
    protected float prevAngleZ;

    protected FlatParticle (ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z);
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(this.spriteProvider);
        this.velocityMultiplier = 0f;
        this.gravityStrength = 0f;
        this.velocityX = 0f;
        this.velocityY = 0f;
        this.velocityZ = 0f;
        this.angleX = 0f;
        this.prevAngleX = 0f;
        this.angleZ = 0f;
        this.prevAngleZ = 0f;
    }

    protected FlatParticle (ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(this.spriteProvider);
    }

    public void setAngleX (float radians) {
        this.prevAngleX = this.angleX;
        this.angleX = radians;
    }

    public void setAngleY (float radians) {
        this.prevAngle = this.angle;
        this.angle = radians;
    }

    public void setAngleZ (float radians) {
        this.prevAngleZ = this.angleZ;
        this.angleZ = radians;
    }

    @Override
    public void tick () {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
        if (this.age > this.maxAge / 2) {
            this.setAlpha(1.0f - ((float)this.age - (float)(this.maxAge / 2)) / (float)this.maxAge);
        }
    }

    @Override
    public ParticleTextureSheet getType () {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    /**
     * Renders a flat, upwards-facing particle.
     * 
     * @param vertexConsumer
     * @param camera
     * @param tickDelta
     */
    @Override
    public void buildGeometry (VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float xLerp = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float yLerp = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float zLerp = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

        Quaternion quaternion = new Quaternion(Vec3f.POSITIVE_X, MathHelper.lerp(tickDelta, this.prevAngleX, this.angleX), false);
        quaternion.hamiltonProduct(new Quaternion(Vec3f.POSITIVE_Y, MathHelper.lerp(tickDelta, this.prevAngle, this.angle), false));
        quaternion.hamiltonProduct(new Quaternion(Vec3f.POSITIVE_Z, MathHelper.lerp(tickDelta, this.prevAngleZ, this.angleZ), false));

        Vec3f[] vector3fs = new Vec3f[] {
            new Vec3f(-1f, 0f, -1f),
            new Vec3f(-1f, 0f, 1f),
            new Vec3f(1f, 0f, 1f),
            new Vec3f(1f, 0f, -1f)
        };

        for (Vec3f vector3f : vector3fs) {
            vector3f.rotate(quaternion);
            vector3f.scale(this.getSize(tickDelta));
            vector3f.add(xLerp, yLerp, zLerp);
        }

        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int brightness = this.getBrightness(tickDelta);
        vertexConsumer.vertex(vector3fs[0].getX(), vector3fs[0].getY(), vector3fs[0].getZ()).texture(maxU, maxV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
        vertexConsumer.vertex(vector3fs[1].getX(), vector3fs[1].getY(), vector3fs[1].getZ()).texture(maxU, minV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
        vertexConsumer.vertex(vector3fs[2].getX(), vector3fs[2].getY(), vector3fs[2].getZ()).texture(minU, minV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
        vertexConsumer.vertex(vector3fs[3].getX(), vector3fs[3].getY(), vector3fs[3].getZ()).texture(minU, maxV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
    }
}
