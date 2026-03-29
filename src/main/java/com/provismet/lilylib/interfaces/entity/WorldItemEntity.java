/*
 * Copyright (C) 2024-2026 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.interfaces.entity;

import com.provismet.lilylib.renderers.WorldItemEntityRenderer;
import net.minecraft.world.entity.projectile.ItemSupplier;

/**
 * <p> Interface intended for Entity subclasses.
 * <p> This should be used by entities represented by an ItemStack, but not intended to be
 * permanently front-facing.
 * 
 * <p> See {@link WorldItemEntityRenderer} for how this is used.
 */
public interface WorldItemEntity extends ItemSupplier {
    default float getXRotation (float tickDelta) {
        return 0f;
    }
    default float getYRotation (float tickDelta) {
        return 0f;
    }
    default float getZRotation (float tickDelta) {
        return 0f;
    }
    default float getXOffset (float tickDelta) {
        return 0f;
    }
    default float getYOffset (float tickDelta) {
        return 0f;
    }
    default float getZOffset (float tickDelta) {
        return 0f;
    }
}
