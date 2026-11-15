/*
 * Copyright (C) 2026 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/26.2/LICENSE for the full license.
 */

package com.provismet.lilylib.container;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

/**
 * A type of container that includes utilities for obtaining Holders from the registry associated with this container's
 * data-type.
 */
public abstract class AbstractRegisteredContainer<T> extends AbstractContainer<T> {
    private final ResourceKey<Registry<T>> registryKey;

    protected AbstractRegisteredContainer (ResourceKey<T> key, ResourceKey<Registry<T>> registryKey) {
        super(key);
        this.registryKey = registryKey;
    }

    /**
     * Creates a generic registry entry for this container.
     * <p>
     * NOTE: This method should only be used if the alternatives are unavailable.
     * This method relies on builtin/bootstrapped registries and is likely to fail if used in gameplay.
     *
     * @return An optional RegistryEntry of this container.
     */
    public Optional<? extends Holder<T>> getEntry () {
        return VanillaRegistries.createWorldLookup()
            .lookup(this.registryKey)
            .flatMap(lookup -> lookup.get(this.getKey()));
    }

    /**
     * Uses a registry manager to obtain an entry for this container.
     *
     * @see net.minecraft.world.level.LevelReader
     *
     * @param manager A registry manager, typically obtained from a World.
     * @return An optional RegistryEntry of this container.
     */
    public Optional<? extends Holder<T>> getEntry (RegistryAccess manager) {
        return manager.lookup(this.registryKey)
            .flatMap(lookup -> lookup.get(this.key));
    }

    /**
     * Uses a registry lookup to obtain an entry for this container.
     *
     * @param provider A lookup, typically obtained from the data generator.
     * @return An optional RegistryEntry of this container.
     */
    public Optional<? extends Holder<T>> getEntry (HolderLookup.Provider provider) {
        return provider.lookup(this.registryKey)
            .flatMap(lookup -> lookup.get(this.key));
    }

    /**
     * Uses a pre-existing registry lookup to obtain the entry for this container.
     *
     * @param registryLookup A RegistryLookup, typically obtained from event hooks or data generators.
     * @return An optional RegistryEntry of this container.
     */
    public Optional<? extends Holder<T>> getEntry (HolderLookup.RegistryLookup<T> registryLookup) {
        return registryLookup.get(this.key);
    }

    /**
     * Creates a generic registry entry for this container.
     *
     * @implNote This method should only be used if the alternatives are unavailable.
     * This method relies on builtin/bootstrapped registries and is likely to fail if used in gameplay.
     *
     * @return An RegistryEntry of this container.
     */
    public Holder<T> getEntryOrThrow () {
        return this.getEntry().orElseThrow();
    }

    /**
     * Uses a registry manager to obtain an entry for this container.
     *
     * @see net.minecraft.world.level.LevelReader
     *
     * @param manager A registry manager, typically obtained from a World.
     * @return An RegistryEntry of this container.
     */
    public Holder<T> getEntryOrThrow (RegistryAccess manager) {
        return this.getEntry(manager).orElseThrow();
    }

    /**
     * Uses a registry lookup to obtain an entry for this container.
     *
     * @param provider A lookup, typically obtained from the data generator.
     * @return An RegistryEntry of this container.
     */
    public Holder<T> getEntryOrThrow (HolderLookup.Provider provider) {
        return this.getEntry(provider).orElseThrow();
    }

    /**
     * Uses a pre-existing registry lookup to obtain the entry for this container.
     *
     * @param registryLookup A RegistryLookup, typically obtained from event hooks or data generators.
     * @return An optional RegistryEntry of this container.
     */
    public Holder<T> getEntryOrThrow (HolderLookup.RegistryLookup<T> registryLookup) {
        return this.getEntry(registryLookup).orElseThrow();
    }
}
