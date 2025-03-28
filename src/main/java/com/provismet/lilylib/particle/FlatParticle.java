/*
 * Copyright (C) 2024-2025 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.particle;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
        this.angleX = -MathHelper.HALF_PI; // Makes the particle face upwards.
        this.prevAngleX = -MathHelper.HALF_PI;
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
        this.lastAngle = this.angle;
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
     * @param vertexConsumer Rendering buffer.
     * @param camera The camera.
     * @param tickDelta The progress from the current tick to the next.
     */
    @Override
    public void render (VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float xLerp = (float)(MathHelper.lerp(tickDelta, this.lastX, this.x) - vec3d.getX());
        float yLerp = (float)(MathHelper.lerp(tickDelta, this.lastY, this.y) - vec3d.getY());
        float zLerp = (float)(MathHelper.lerp(tickDelta, this.lastZ, this.z) - vec3d.getZ());

        Quaternionf quaternion = new Quaternionf();
        quaternion.rotateX(MathHelper.lerp(tickDelta, this.prevAngleX, this.angleX));
        quaternion.rotateY(MathHelper.lerp(tickDelta, this.lastAngle, this.angle));
        quaternion.rotateZ(MathHelper.lerp(tickDelta, this.prevAngleZ, this.angleZ));

        Vector3f[] vector3fs = new Vector3f[] {
            new Vector3f(-1f, 0f, -1f),
            new Vector3f(-1f, 0f, 1f),
            new Vector3f(1f, 0f, 1f),
            new Vector3f(1f, 0f, -1f)
        };

        for (Vector3f vector3f : vector3fs) {
            vector3f.rotate(quaternion);
            vector3f.mul(this.getSize(tickDelta));
            vector3f.add(xLerp, yLerp, zLerp);
        }

        this.render(vertexConsumer, camera, quaternion, tickDelta);
    }
}
