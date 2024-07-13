/*
 * Copyright (C) 2024 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.interfaces.entity;

import com.provismet.lilylib.renderers.WorldItemEntityRenderer;
import net.minecraft.entity.FlyingItemEntity;

/**
 * <p> Interface intended for Entity subclasses.
 * <p> This should be used by entities represented by an ItemStack, but not intended to be
 * permanently front-facing.
 * 
 * <p> See {@link WorldItemEntityRenderer} for how this is used.
 */
public interface WorldItemEntity extends FlyingItemEntity {
    public default float getXRotation (float tickDelta) {
        return 0f;
    }

    public default float getYRotation (float tickDelta) {
        return 0f;
    }

    public default float getZRotation (float tickDelta) {
        return 0f;
    }

    public default float getXOffset (float tickDelta) {
        return 0f;
    }
    
    public default float getYOffset (float tickDelta) {
        return 0f;
    }
    
    public default float getZOffset (float tickDelta) {
        return 0f;
    }
}
