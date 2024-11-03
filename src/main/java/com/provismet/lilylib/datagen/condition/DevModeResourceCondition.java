/*
 * Copyright (C) 2024 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.datagen.condition;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;

public record DevModeResourceCondition () implements ResourceCondition {
    public static final MapCodec<DevModeResourceCondition> CODEC = MapCodec.unit(DevModeResourceCondition::new);

    @Override
    public ResourceConditionType<?> getType () {
        return LilyResourceConditions.DEVELOPMENT_MODE_ENABLED;
    }

    @Override
    public boolean test (@Nullable RegistryOps.RegistryInfoGetter registryInfoGetter) {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
