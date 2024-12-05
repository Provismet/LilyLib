/*
 * Copyright (C) 2024 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.datagen.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Enables data generation for equipment assets.
 * <p>
 * Based on vanilla's provider: {@link net.minecraft.client.data.EquipmentAssetProvider}
 */
public abstract class LilyEquipmentAssetProvider implements DataProvider {
    private final DataOutput.PathResolver pathResolver;

    public LilyEquipmentAssetProvider (FabricDataOutput output) {
        this.pathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "equipment");
    }

    @Override
    public CompletableFuture<?> run (DataWriter writer) {
        Map<Identifier, EquipmentModel> map = new HashMap<>();
        this.generate(map::putIfAbsent);

        return DataProvider.writeAllToPath(writer, EquipmentModel.CODEC, this.pathResolver, map);
    }

    @Override
    public String getName () {
        return "Lily Equipment Assets";
    }

    protected abstract void generate (BiConsumer<Identifier, EquipmentModel> consumer);

    protected EquipmentModel buildHumanoid (Identifier modelId) {
        return EquipmentModel.builder().addHumanoidLayers(modelId).build();
    }

    protected EquipmentModel buildHumanoidAndHorse (Identifier modelId) {
        return EquipmentModel.builder()
            .addHumanoidLayers(modelId)
            .addLayers(EquipmentModel.LayerType.HORSE_BODY, EquipmentModel.Layer.createWithLeatherColor(modelId, false))
            .build();
    }
}
