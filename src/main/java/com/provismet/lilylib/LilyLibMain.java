/*
 * Copyright (C) 2024 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib;

import net.minecraft.util.Identifier;

public class LilyLibMain {
    public static final String MODID = "lilylib";

    public static Identifier identifier (String path) {
        return Identifier.of(MODID, path);
    }
}
