/*
 * Copyright (C) 2024 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.datagen.provider;

import com.provismet.lilylib.container.DamageTypeContainer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public abstract class LilyDamageTypeProvider extends FabricDynamicRegistryProvider {
    protected LilyDamageTypeProvider (FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure (RegistryWrapper.WrapperLookup registries, Entries entries) {
        DamageConsumer consumer = new DamageConsumer(entries);
        this.generate(registries, consumer);
    }

    protected abstract void generate (RegistryWrapper.WrapperLookup registries, DamageConsumer consumer);

    @Override
    public String getName () {
        return "damage_type";
    }

    public static class DamageConsumer {
        private final Entries entries;

        protected DamageConsumer (Entries entries) {
            this.entries = entries;
        }

        public void add (RegistryKey<DamageType> key, DamageType damageType) {
            this.entries.add(key, damageType);
        }

        public void add (Identifier id, DamageType damageType) {
            this.add(RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id), damageType);
        }

        public void add (DamageTypeContainer container) {
            this.add(container.getKey(), container.getDamageType());
        }
    }
}
