/*
 * Copyright (C) 2024-2026 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.21.10/LICENSE for the full license.
 */

package com.provismet.lilylib.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * <p> A particle that renders flat on the ground.
 * <p> Supports animated sprites.
 */
public abstract class FlatParticle extends SingleQuadParticle {
    protected final SpriteSet spriteProvider;

    protected float angleX;
    protected float prevAngleX;
    protected float angleZ;
    protected float prevAngleZ;

    protected FlatParticle (ClientLevel clientWorld, double x, double y, double z, SpriteSet spriteProvider) {
        super(clientWorld, x, y, z, spriteProvider.first());
        this.spriteProvider = spriteProvider;
        this.setSpriteFromAge(this.spriteProvider);
        this.friction = 0f;
        this.gravity = 0f;
        this.xd = 0f;
        this.yd = 0f;
        this.zd = 0f;
        this.angleX = -Mth.HALF_PI; // Makes the particle face upwards.
        this.prevAngleX = -Mth.HALF_PI;
        this.angleZ = 0f;
        this.prevAngleZ = 0f;
    }

    protected FlatParticle (ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
        super(clientWorld, x, y, z, velocityX, velocityY, velocityZ, spriteProvider.first());
        this.spriteProvider = spriteProvider;
        this.setSpriteFromAge(this.spriteProvider);
    }

    public void setAngleX (float radians) {
        this.prevAngleX = this.angleX;
        this.angleX = radians;
    }

    public void setAngleY (float radians) {
        this.oRoll = this.roll;
        this.roll = radians;
    }

    public void setAngleZ (float radians) {
        this.prevAngleZ = this.angleZ;
        this.angleZ = radians;
    }

    @Override
    public void tick () {
        super.tick();
        this.setSpriteFromAge(this.spriteProvider);
        if (this.age > this.lifetime / 2) {
            this.setAlpha(1.0f - ((float)this.age - (float)(this.lifetime / 2)) / (float)this.lifetime);
        }
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    /**
     * Renders a flat, upwards-facing particle.
     * 
     * @param submittable Render information.
     * @param camera The camera.
     * @param tickDelta The progress from the current tick to the next.
     */
    @Override
    public void extract (QuadParticleRenderState submittable, Camera camera, float tickDelta) {
        Vec3 vec3d = camera.position();
        float xLerp = (float)(Mth.lerp(tickDelta, this.xo, this.x) - vec3d.x());
        float yLerp = (float)(Mth.lerp(tickDelta, this.yo, this.y) - vec3d.y());
        float zLerp = (float)(Mth.lerp(tickDelta, this.zo, this.z) - vec3d.z());

        Quaternionf quaternion = new Quaternionf();
        quaternion.rotateX(Mth.lerp(tickDelta, this.prevAngleX, this.angleX));
        quaternion.rotateY(Mth.lerp(tickDelta, this.oRoll, this.roll));
        quaternion.rotateZ(Mth.lerp(tickDelta, this.prevAngleZ, this.angleZ));

        Vector3f[] vector3fs = new Vector3f[] {
            new Vector3f(-1f, 0f, -1f),
            new Vector3f(-1f, 0f, 1f),
            new Vector3f(1f, 0f, 1f),
            new Vector3f(1f, 0f, -1f)
        };

        for (Vector3f vector3f : vector3fs) {
            vector3f.rotate(quaternion);
            vector3f.mul(this.getQuadSize(tickDelta));
            vector3f.add(xLerp, yLerp, zLerp);
        }

        this.extractRotatedQuad(submittable, camera, quaternion, tickDelta);
    }
}
