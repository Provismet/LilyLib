package com.provismet.lilylib.container;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * General utility class for making DamageTypes easier to use.
 * <p>
 * The DamageTypeContainer links a {@link RegistryKey<DamageType>} to a {@link DamageType},
 * helping to reduce code duplication between data generation and bootstrapping for damage types.
 * <p>
 * The container additionally helps with handling damage sources and translation keys.
 *
 * @see RegistryKey
 * @see DamageType
 */
public class DamageTypeContainer extends AbstractContainer<DamageType> {
    private final DamageType type;

    public DamageTypeContainer (Identifier id, DamageType type) {
        this(RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id), type);
    }

    public DamageTypeContainer (RegistryKey<DamageType> key, DamageType type) {
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
        return sources.create(this.key);
    }

    /**
     * Creates a damage source caused by an attacking entity.
     *
     * @param attacker The entity that performed the attack.
     * @return A new damage source for this damage type.
     */
    public DamageSource createDamageSource (Entity attacker) {
        return attacker.getDamageSources().create(this.key, attacker);
    }

    /**
     * Creates a damage source caused by source entity
     *
     * @param directAttacker The direct cause of the damage (such as a projectile)
     * @param attacker The owner or shooter of the directAttacker.
     * @return A new damage source for this damage type.
     */
    public DamageSource createDamageSource (Entity directAttacker, @Nullable Entity attacker) {
        return directAttacker.getDamageSources().create(this.key, directAttacker, attacker);
    }

    /**
     * Returns a generic death message for this damage type.
     *
     * @return "death.attack.{damage type id}"
     */
    public String getDeathTranslationKey () {
        return "death.attack." + this.type.msgId();
    }

    public String getDeathTranslationKey (String suffix) {
        return this.getDeathTranslationKey() + "." + suffix;
    }
}
