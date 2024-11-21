/*
 * Copyright (C) 2024 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.container;

import com.provismet.lilylib.datagen.provider.LilyEnchantmentProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Utility class for linking a registry key to an enchantment builder.
 * <p>
 * The EnchantmentContainer allows the enchantment builder to be reused between data-gen and in bootstrapping.
 * <p>
 * The EnchantmentContainer can be fed directly to the {} during
 * data generation.
 *
 * @see RegistryKey
 * @see Enchantment.Builder
 */
public class EnchantmentContainer extends AbstractContainer<Enchantment> {
    private final BuilderBuilder internalBuilder;

    public EnchantmentContainer (Identifier id, BuilderBuilder builder) {
        this(RegistryKey.of(RegistryKeys.ENCHANTMENT, id), builder);
    }

    public EnchantmentContainer (RegistryKey<Enchantment> key, BuilderBuilder builder) {
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
    public Optional<? extends RegistryEntry<Enchantment>> getEntry () {
        return BuiltinRegistries.createWrapperLookup().getOrThrow(RegistryKeys.ENCHANTMENT).getOptional(this.getKey());
    }

    /**
     * Uses a registry manager to obtain an entry for this enchantment.
     *
     * @see net.minecraft.world.WorldView
     *
     * @param manager A registry manager, typically obtained from a World.
     * @return An optional RegistryEntry of this enchantment.
     */
    public Optional<? extends RegistryEntry<Enchantment>> getEntry (DynamicRegistryManager manager) {
        return manager.getOrThrow(RegistryKeys.ENCHANTMENT).getOptional(this.key);
    }

    /**
     * Uses a registry lookup to obtain an entry for this enchantment.
     *
     * @param registryLookup A lookup, typically obtained from the data generator.
     * @return An optional RegistryEntry of this enchantment.
     */
    public Optional<? extends RegistryEntry<Enchantment>> getEntry (RegistryWrapper.WrapperLookup registryLookup) {
        return registryLookup.getOrThrow(RegistryKeys.ENCHANTMENT).getOptional(this.key);
    }

    /**
     * Creates a generic registry entry for this enchantment.
     *
     * @implNote This method should only be used if the alternatives are unavailable.
     * This method relies on builtin/bootstrapped registries and is likely to fail if used in gameplay.
     *
     * @return An RegistryEntry of this enchantment.
     */
    public RegistryEntry<Enchantment> getEntryOrThrow () {
        return this.getEntry().orElseThrow();
    }

    /**
     * Uses a registry manager to obtain an entry for this enchantment.
     *
     * @see net.minecraft.world.WorldView
     *
     * @param manager A registry manager, typically obtained from a World.
     * @return An RegistryEntry of this enchantment.
     */
    public RegistryEntry<Enchantment> getEntryOrThrow (DynamicRegistryManager manager) {
        return this.getEntry(manager).orElseThrow();
    }

    /**
     * Uses a registry lookup to obtain an entry for this enchantment.
     *
     * @param registryLookup A lookup, typically obtained from the data generator.
     * @return An RegistryEntry of this enchantment.
     */
    public RegistryEntry<Enchantment> getEntryOrThrow (RegistryWrapper.WrapperLookup registryLookup) {
        return this.getEntry(registryLookup).orElseThrow();
    }

    /**
     * Unwraps a registerable provided by the vanilla bootstrapper to produce the enchantment builder associated with this container.
     *
     * @param registerable The registry object provided by the vanilla bootstrapper.
     * @return The enchantment builder.
     */
    public Enchantment.Builder getBuilder (Registerable<Enchantment> registerable) {
        RegistryEntryLookup<Item> itemLookup = registerable.getRegistryLookup(RegistryKeys.ITEM);
        RegistryEntryLookup<Enchantment> enchantmentLookup = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        RegistryEntryLookup<DamageType> damageLookup = registerable.getRegistryLookup(RegistryKeys.DAMAGE_TYPE);
        RegistryEntryLookup<Block> blockLookup = registerable.getRegistryLookup(RegistryKeys.BLOCK);
        RegistryEntryLookup<EntityType<?>> entityLookup = registerable.getRegistryLookup(RegistryKeys.ENTITY_TYPE);
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
    public Enchantment.Builder getBuilder (RegistryEntryLookup<Item> itemLookup, RegistryEntryLookup<Enchantment> enchantmentLookup, RegistryEntryLookup<DamageType> damageLookup, RegistryEntryLookup<Block> blockLookup, RegistryEntryLookup<EntityType<?>> entityLookup) {
        return this.internalBuilder.create(itemLookup, enchantmentLookup, damageLookup, blockLookup, entityLookup);
    }

    @Override
    public String getTranslationKey () {
        return this.key.getValue().toTranslationKey("enchantment");
    }

    @Override
    public String getTranslationKey (String suffix) {
        return this.key.getValue().toTranslationKey("enchantment", suffix);
    }

    @FunctionalInterface
    public interface BuilderBuilder {
        Enchantment.Builder create (
            RegistryEntryLookup<Item> itemLookup,
            RegistryEntryLookup<Enchantment> enchantmentLookup,
            RegistryEntryLookup<DamageType> damageLookup,
            RegistryEntryLookup<Block> blockLookup,
            RegistryEntryLookup<EntityType<?>> entityLookup
        );
    }
}
