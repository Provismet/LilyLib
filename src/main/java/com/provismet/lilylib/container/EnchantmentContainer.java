/*
 * Copyright (C) 2024-2026 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.container;

import com.provismet.lilylib.datagen.provider.LilyEnchantmentProvider;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

/**
 * Utility class for linking a registry key to an enchantment builder.
 * <p>
 * The EnchantmentContainer allows the enchantment builder to be reused between data-gen and in bootstrapping.
 * <p>
 * The EnchantmentContainer can be fed directly to the {} during
 * data generation.
 *
 * @see ResourceKey
 * @see Enchantment.Builder
 */
public class EnchantmentContainer extends AbstractContainer<Enchantment> {
    private final BuilderBuilder internalBuilder;

    public EnchantmentContainer (Identifier id, BuilderBuilder builder) {
        this(ResourceKey.create(Registries.ENCHANTMENT, id), builder);
    }

    public EnchantmentContainer (ResourceKey<Enchantment> key, BuilderBuilder builder) {
        super(key);
        this.internalBuilder = builder;
    }

    /**
     * Creates a generic registry entry for this enchantment.
     * <p>
     * NOTE: This method should only be used if the alternatives are unavailable.
     * This method relies on builtin/bootstrapped registries and is likely to fail if used in gameplay.
     *
     * @return An optional RegistryEntry of this enchantment.
     */
    public Optional<? extends Holder<Enchantment>> getEntry () {
        return VanillaRegistries.createLookup().lookupOrThrow(Registries.ENCHANTMENT).get(this.getKey());
    }

    /**
     * Uses a registry manager to obtain an entry for this enchantment.
     *
     * @see net.minecraft.world.level.LevelReader
     *
     * @param manager A registry manager, typically obtained from a World.
     * @return An optional RegistryEntry of this enchantment.
     */
    public Optional<? extends Holder<Enchantment>> getEntry (RegistryAccess manager) {
        return manager.lookupOrThrow(Registries.ENCHANTMENT).get(this.key);
    }

    /**
     * Uses a registry lookup to obtain an entry for this enchantment.
     *
     * @param registryLookup A lookup, typically obtained from the data generator.
     * @return An optional RegistryEntry of this enchantment.
     */
    public Optional<? extends Holder<Enchantment>> getEntry (HolderLookup.Provider registryLookup) {
        return registryLookup.lookupOrThrow(Registries.ENCHANTMENT).get(this.key);
    }

    /**
     * Creates a generic registry entry for this enchantment.
     *
     * @implNote This method should only be used if the alternatives are unavailable.
     * This method relies on builtin/bootstrapped registries and is likely to fail if used in gameplay.
     *
     * @return An RegistryEntry of this enchantment.
     */
    public Holder<Enchantment> getEntryOrThrow () {
        return this.getEntry().orElseThrow();
    }

    /**
     * Uses a registry manager to obtain an entry for this enchantment.
     *
     * @see net.minecraft.world.level.LevelReader
     *
     * @param manager A registry manager, typically obtained from a World.
     * @return An RegistryEntry of this enchantment.
     */
    public Holder<Enchantment> getEntryOrThrow (RegistryAccess manager) {
        return this.getEntry(manager).orElseThrow();
    }

    /**
     * Uses a registry lookup to obtain an entry for this enchantment.
     *
     * @param registryLookup A lookup, typically obtained from the data generator.
     * @return An RegistryEntry of this enchantment.
     */
    public Holder<Enchantment> getEntryOrThrow (HolderLookup.Provider registryLookup) {
        return this.getEntry(registryLookup).orElseThrow();
    }

    /**
     * Unwraps a registerable provided by the vanilla bootstrapper to produce the enchantment builder associated with this container.
     *
     * @param registerable The registry object provided by the vanilla bootstrapper.
     * @return The enchantment builder.
     */
    public Enchantment.Builder getBuilder (BootstrapContext<Enchantment> registerable) {
        HolderGetter<Item> itemLookup = registerable.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantmentLookup = registerable.lookup(Registries.ENCHANTMENT);
        HolderGetter<DamageType> damageLookup = registerable.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<Block> blockLookup = registerable.lookup(Registries.BLOCK);
        HolderGetter<EntityType<?>> entityLookup = registerable.lookup(Registries.ENTITY_TYPE);
        return this.getBuilder(itemLookup, enchantmentLookup, damageLookup, blockLookup, entityLookup);
    }

    /**
     * Unwraps the Combat+ data-gen EnchantmentBuilder to produce the enchantment builder associated with this container.
     *
     * @see LilyEnchantmentProvider
     *
     * @param enchantmentBuilder The builder provided by the Combat+ provider.
     * @return The enchantment builder.
     */
    public Enchantment.Builder getBuilder (LilyEnchantmentProvider.EnchantmentBuilder enchantmentBuilder) {
        return this.getBuilder(
            enchantmentBuilder.itemLookup,
            enchantmentBuilder.enchantmentLookup,
            enchantmentBuilder.damageTypeLookup,
            enchantmentBuilder.blockLookup,
            enchantmentBuilder.entityLookup
        );
    }

    /**
     * Creates the enchantment builder associated with this container.
     *
     * @param itemLookup Registry lookup for item tags.
     * @param enchantmentLookup Registry lookup for enchantment tags.
     * @param damageLookup Registry lookup for damage type tags.
     * @param blockLookup  Registry lookup for block tags.
     * @return The enchantment builder.
     */
    public Enchantment.Builder getBuilder (HolderGetter<Item> itemLookup, HolderGetter<Enchantment> enchantmentLookup, HolderGetter<DamageType> damageLookup, HolderGetter<Block> blockLookup, HolderGetter<EntityType<?>> entityLookup) {
        return this.internalBuilder.create(itemLookup, enchantmentLookup, damageLookup, blockLookup, entityLookup);
    }

    @Override
    public String getTranslationKey () {
        return this.key.identifier().toLanguageKey("enchantment");
    }

    @Override
    public String getTranslationKey (String suffix) {
        return this.key.identifier().toLanguageKey("enchantment", suffix);
    }

    @FunctionalInterface
    public interface BuilderBuilder {
        Enchantment.Builder create (
            HolderGetter<Item> itemLookup,
            HolderGetter<Enchantment> enchantmentLookup,
            HolderGetter<DamageType> damageLookup,
            HolderGetter<Block> blockLookup,
            HolderGetter<EntityType<?>> entityLookup
        );
    }
}
