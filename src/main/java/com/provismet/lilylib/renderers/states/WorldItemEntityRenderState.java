/*
 * Copyright (C) 2026 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.renderers.states;

import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;

/**
 * All rotations are in degrees.
 */
public class WorldItemEntityRenderState extends ItemEntityRenderState {
    public float xRotation;
    public float yRotation;
    public float zRotation;
    public float xOffset;
    public float yOffset;
    public float zOffset;
}
