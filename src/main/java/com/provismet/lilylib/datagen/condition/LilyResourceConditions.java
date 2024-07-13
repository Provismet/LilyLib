package com.provismet.lilylib.datagen.condition;

import com.mojang.serialization.MapCodec;
import com.provismet.lilylib.LilyLibMain;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

public abstract class LilyResourceConditions {
    private static boolean registered = false;

    public static final ResourceConditionType<DevModeResourceCondition> DEVELOPMENT_MODE_ENABLED = createResourceConditionType("developer_mode", DevModeResourceCondition.CODEC);

    /**
     * LilyLib does not register anything by itself.
     * <p>
     * Call this method if your mod needs to use the LilyLib resource conditions.
     */
    public static void register () {
        if (LilyResourceConditions.registered) return;
        LilyResourceConditions.registered = true;

        ResourceConditions.register(DEVELOPMENT_MODE_ENABLED);
    }

    private static <T extends ResourceCondition> ResourceConditionType<T> createResourceConditionType (String name, MapCodec<T> codec) {
        return ResourceConditionType.create(LilyLibMain.identifier(name), codec);
    }
}
