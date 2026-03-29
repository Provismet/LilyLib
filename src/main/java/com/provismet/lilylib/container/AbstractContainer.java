/*
 * Copyright (C) 2024-2026 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.container;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public abstract class AbstractContainer<T> {
    protected final ResourceKey<T> key;

    protected AbstractContainer (ResourceKey<T> key) {
        this.key = key;
    }

    /**
     * @return The registry key associated with this container.
     */
    public ResourceKey<T> getKey () {
        return this.key;
    }

    /**
     * @return The identifier held in this container's registry key.
     */
    public Identifier getIdentifier () {
        return this.key.identifier();
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
