package com.provismet.lilylib.container;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public abstract class AbstractContainer<T> {
    protected final RegistryKey<T> key;

    protected AbstractContainer (RegistryKey<T> key) {
        this.key = key;
    }

    public RegistryKey<T> getKey () {
        return this.key;
    }

    public Identifier getIdentifier () {
        return this.key.getValue();
    }
}
