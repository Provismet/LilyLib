package com.provismet.lilylib.container;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public abstract class AbstractContainer<T> {
    protected final RegistryKey<T> key;

    protected AbstractContainer (RegistryKey<T> key) {
        this.key = key;
    }

    /**
     * @return The registry key associated with this container.
     */
    public RegistryKey<T> getKey () {
        return this.key;
    }

    /**
     * @return The identifier held in this container's registry key.
     */
    public Identifier getIdentifier () {
        return this.key.getValue();
    }

    /**
     * Creates the translation key for this container.
     *
     * @return The translation key.
     */
    public abstract String getTranslationKey ();

    /**
     * Creates a suffixed translation key for this container.
     * <p>
     * The resulting key will have {@code .<suffix>} added to the end.
     *
     * @param suffix The string to append to the translation key.
     * @return The translation key.
     */
    public String getTranslationKey (String suffix) {
        return this.getTranslationKey() + "." + suffix;
    }
}
