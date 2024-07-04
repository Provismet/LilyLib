package com.provismet.lilylib;

import net.minecraft.util.Identifier;

public class LilyLibMain {
    public static final String MODID = "lilylib";

    public static Identifier identifier (String path) {
        return Identifier.of(MODID, path);
    }
}
