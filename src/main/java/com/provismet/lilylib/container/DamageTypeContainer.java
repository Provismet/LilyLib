/*
 * Copyright (C) 2024-2026 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.container;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

/**
 * General utility class for making DamageTypes easier to use.
 * <p>
 * The DamageTypeContainer links a {@link ResourceKey<DamageType>} to a {@link DamageType},
 * helping to reduce code duplication between data generation and bootstrapping for damage types.
 * <p>
 * The container additionally helps with handling damage sources and translation keys.
 *
 * @see ResourceKey
 * @see DamageType
 */
public class DamageTypeContainer extends AbstractContainer<DamageType> {
    private final DamageType type;

    public DamageTypeContainer (Identifier id, DamageType type) {
        this(ResourceKey.create(Registries.DAMAGE_TYPE, id), type);
    }

    public DamageTypeContainer (ResourceKey<DamageType> key, DamageType type) {
        super(key);
        this.type = type;
    }

    public DamageType getDamageType () {
        return this.type;
    }

    /**
     * Creates a damage source not associated with any entity.
     *
     * @param sources The sources object to create this source from.
     * @return A new damage source for this damage type.
     */
    public DamageSource createDamageSource (DamageSources sources) {
        return sources.source(this.key);
    }

    /**
     * Creates a damage source caused by an attacking entity.
     *
     * @param attacker The entity that performed the attack.
     * @return A new damage source for this damage type.
     */
    public DamageSource createDamageSource (Entity attacker) {
        return attacker.damageSources().source(this.key, attacker);
    }

    /**
     * Creates a damage source caused by source entity
     *
     * @param directAttacker The direct cause of the damage (such as a projectile)
     * @param attacker The owner or shooter of the directAttacker.
     * @return A new damage source for this damage type.
     */
    public DamageSource createDamageSource (Entity directAttacker, @Nullable Entity attacker) {
        return directAttacker.damageSources().source(this.key, directAttacker, attacker);
    }

    /**
     * Returns a generic death message for this damage type.
     *
     * @return {@code "death.attack.<damage type id>"}
     */
    @Override
    public String getTranslationKey () {
        return "death.attack." + this.type.msgId();
    }

    /**
     * Returns a generic death message for this damage type, with a suffix appended.
     * <p>
     * Standard vanilla suffixes are "item" and "player".
     *
     * @param suffix {@code .<suffix>} will be appended to the end of the translation key.
     * @return {@code "death.attack.<damage type id>.<suffix>"}
     */
    @Override
    public String getTranslationKey (String suffix) {
        return this.getTranslationKey() + "." + suffix;
    }
}
