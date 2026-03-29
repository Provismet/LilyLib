/*
 * Copyright (C) 2024-2026 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21.10/LICENSE for the full license.
 */

package com.provismet.lilylib.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.concurrent.CompletableFuture;

public abstract class LilyTagProviders {
    public static abstract class LilyDamageTypeTagProvider extends FabricTagProvider<DamageType> {
        public LilyDamageTypeTagProvider (FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, Registries.DAMAGE_TYPE, registriesFuture);
        }
    }

    public static abstract class LilyEnchantmentTagProvider extends FabricTagProvider<Enchantment> {
        public LilyEnchantmentTagProvider (FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, Registries.ENCHANTMENT, registriesFuture);
        }
    }
}
