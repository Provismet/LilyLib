/*
 * Copyright (C) 2024-2026 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.datagen.provider;

import com.provismet.lilylib.container.EnchantmentContainer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import java.util.concurrent.CompletableFuture;

public abstract class LilyEnchantmentProvider extends FabricDynamicRegistryProvider {
    protected LilyEnchantmentProvider (FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public final String getName () {
        return "enchantment";
    }

    @Override
    protected final void configure (HolderLookup.Provider wrapperLookup, Entries entries) {
        EnchantmentBuilder builder = new EnchantmentBuilder(entries);
        generate(wrapperLookup, builder);
    }

    protected abstract void generate (HolderLookup.Provider wrapperLookup, EnchantmentBuilder builder);

    public static class EnchantmentBuilder {
        public final HolderGetter<Item> itemLookup;
        public final HolderGetter<DamageType> damageTypeLookup;
        public final HolderGetter<Block> blockLookup;
        public final HolderGetter<Enchantment> enchantmentLookup;
        public final HolderGetter<EntityType<?>> entityLookup;

        private final Entries entries;

        protected EnchantmentBuilder (Entries entries) {
            this.entries = entries;
            this.itemLookup = entries.getLookup(Registries.ITEM);
            this.damageTypeLookup = entries.getLookup(Registries.DAMAGE_TYPE);
            this.blockLookup = entries.getLookup(Registries.BLOCK);
            this.enchantmentLookup = entries.getLookup(Registries.ENCHANTMENT);
            this.entityLookup = entries.getLookup(Registries.ENTITY_TYPE);
        }

        public void add (Identifier id, Enchantment.Builder enchantmentBuilder) {
            this.entries.add(ResourceKey.create(Registries.ENCHANTMENT, id), enchantmentBuilder.build(id));
        }

        public void add (Identifier id, Enchantment.Builder enchantmentBuilder, ResourceCondition... conditions) {
            this.entries.add(ResourceKey.create(Registries.ENCHANTMENT, id), enchantmentBuilder.build(id), conditions);
        }

        public void add (ResourceKey<Enchantment> enchantmentKey, Enchantment.Builder enchantmentBuilder) {
            this.add(enchantmentKey.identifier(), enchantmentBuilder);
        }

        public void add (ResourceKey<Enchantment> enchantmentKey, Enchantment.Builder enchantmentBuilder, ResourceCondition... conditions) {
            this.add(enchantmentKey.identifier(), enchantmentBuilder, conditions);
        }

        public void add (EnchantmentContainer container) {
            this.add(container.getKey(), container.getBuilder(this));
        }

        public void add (EnchantmentContainer container, ResourceCondition... conditions) {
            this.add(container.getKey(), container.getBuilder(this), conditions);
        }

        public HolderSet<Item> getItemEntryList (TagKey<Item> tag) {
            return this.itemLookup.getOrThrow(tag);
        }

        public HolderSet<DamageType> getDamageTypeEntryList (TagKey<DamageType> tag) {
            return this.damageTypeLookup.getOrThrow(tag);
        }

        public HolderSet<Block> getBlockEntryList (TagKey<Block> tag) {
            return this.blockLookup.getOrThrow(tag);
        }

        public HolderSet<Enchantment> getEnchantmentEntryList (TagKey<Enchantment> tag) {
            return this.enchantmentLookup.getOrThrow(tag);
        }
    }
}
