/*
 * Copyright (C) 2024-2025 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21.10/LICENSE for the full license.
 */

package com.provismet.lilylib.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public abstract class LilyTagProviders {
    public static abstract class LilyDamageTypeTagProvider extends FabricTagProvider<DamageType> {
        public LilyDamageTypeTagProvider (FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
        }
    }

    public static abstract class LilyEnchantmentTagProvider extends FabricTagProvider<Enchantment> {
        public LilyEnchantmentTagProvider (FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
        }
    }
}
