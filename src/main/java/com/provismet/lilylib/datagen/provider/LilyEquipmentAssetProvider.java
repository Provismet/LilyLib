/*
 * Copyright (C) 2024-2026 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.datagen.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Enables data generation for equipment assets.
 * <p>
 * Based on vanilla's provider: {@link net.minecraft.client.data.models.EquipmentAssetProvider}
 */
public abstract class LilyEquipmentAssetProvider implements DataProvider {
    private final PackOutput.PathProvider pathResolver;

    public LilyEquipmentAssetProvider (FabricDataOutput output) {
        this.pathResolver = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    @Override
    public CompletableFuture<?> run (CachedOutput writer) {
        Map<Identifier, EquipmentClientInfo> map = new HashMap<>();
        this.generate(map::putIfAbsent);

        return DataProvider.saveAll(writer, EquipmentClientInfo.CODEC, this.pathResolver, map);
    }

    @Override
    public String getName () {
        return "Lily Equipment Assets";
    }

    protected abstract void generate (BiConsumer<Identifier, EquipmentClientInfo> consumer);

    protected EquipmentClientInfo buildHumanoid (Identifier modelId) {
        return EquipmentClientInfo.builder().addHumanoidLayers(modelId).build();
    }

    protected EquipmentClientInfo buildHumanoidAndHorse (Identifier modelId) {
        return EquipmentClientInfo.builder()
            .addHumanoidLayers(modelId)
            .addLayers(EquipmentClientInfo.LayerType.HORSE_BODY, EquipmentClientInfo.Layer.leatherDyeable(modelId, false))
            .build();
    }
}
