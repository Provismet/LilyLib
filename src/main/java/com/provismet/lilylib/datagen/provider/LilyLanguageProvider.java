/*
 * Copyright (C) 2024-2026 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.datagen.provider;

import com.provismet.lilylib.container.DamageTypeContainer;
import com.provismet.lilylib.container.EnchantmentContainer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public abstract class LilyLanguageProvider extends FabricLanguageProvider {
    protected LilyLanguageProvider (FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    protected LilyLanguageProvider (FabricDataOutput dataOutput, String languageCode, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, languageCode, registryLookup);
    }

    protected void addEnchantment (TranslationBuilder translationBuilder, EnchantmentContainer container, String name, String description) {
        translationBuilder.add(container.getTranslationKey(), name);
        translationBuilder.add(container.getTranslationKey("desc"), description);
        translationBuilder.add(container.getTranslationKey("description"), description);
    }

    protected void addDeathMessage (TranslationBuilder translationBuilder, DamageTypeContainer container, String normalMessage, String itemMessage) {
        this.addDeathMessage(translationBuilder, container, normalMessage, null, itemMessage);
    }

    protected void addDeathMessage (TranslationBuilder translationBuilder, DamageTypeContainer container, String normalMessage, @Nullable String playerMessage, String itemMessage) {
        translationBuilder.add(container.getTranslationKey(), normalMessage);
        if (playerMessage != null) translationBuilder.add(container.getTranslationKey("player"), playerMessage);
        translationBuilder.add(container.getTranslationKey("item"), itemMessage);
    }
}
