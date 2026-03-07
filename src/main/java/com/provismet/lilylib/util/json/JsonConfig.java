/*
 * Copyright (C) 2026 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Reusable json config handler. The handler respects the ordering of the entries provided and will construct/read json files in that order.
 */
public class JsonConfig {
    private final List<ConfigEntry<?>> entries;

    public JsonConfig () {
        this.entries = new ArrayList<>();
    }

    public JsonConfig addString (String key, Supplier<String> saveSupplier, Consumer<String> loadConsumer) {
        return this.addGeneric(key, saveSupplier, loadConsumer, JsonReader::getString);
    }

    public JsonConfig addBoolean (String key, Supplier<Boolean> saveSupplier, Consumer<Boolean> loadConsumer) {
        return this.addGeneric(key, saveSupplier, loadConsumer, JsonReader::getBoolean);
    }

    public JsonConfig addInteger (String key, Supplier<Integer> saveSupplier, Consumer<Integer> loadConsumer) {
        return this.addGeneric(key, saveSupplier, loadConsumer, JsonReader::getInteger);
    }

    public JsonConfig addDouble (String key, Supplier<Double> saveSupplier, Consumer<Double> loadConsumer) {
        return this.addGeneric(key, saveSupplier, loadConsumer, JsonReader::getDouble);
    }

    public JsonConfig addFloat (String key, Supplier<Float> saveSupplier, Consumer<Float> loadConsumer) {
        return this.addGeneric(key, saveSupplier, loadConsumer, JsonReader::getFloat);
    }

    public JsonConfig addLong (String key, Supplier<Long> saveSupplier, Consumer<Long> loadConsumer) {
        return this.addGeneric(key, saveSupplier, loadConsumer, JsonReader::getLong);
    }

    public JsonConfig addShort (String key, Supplier<Short> saveSupplier, Consumer<Short> loadConsumer) {
        return this.addGeneric(key, saveSupplier, loadConsumer, JsonReader::getShort);
    }

    public JsonConfig addByte (String key, Supplier<Byte> saveSupplier, Consumer<Byte> loadConsumer) {
        return this.addGeneric(key, saveSupplier, loadConsumer, JsonReader::getByte);
    }

    public JsonConfig addJsonObject (String key, JsonConfig handler) {
        return this.addGeneric(key, handler::createJson, handler::loadFromJson, JsonReader::getObject);
    }

    public JsonConfig addJson (String key, Supplier<JsonElement> saveSupplier, Consumer<JsonElement> loadConsumer) {
        return this.addGeneric(key, saveSupplier, loadConsumer, JsonReader::get);
    }

    public <T> JsonConfig addGeneric (String key, Supplier<T> saveSupplier, Consumer<T> loadConsumer, BiFunction<JsonReader, String, Optional<T>> getter) {
        this.entries.add(new ConfigEntry<>(key, saveSupplier, reader -> getter.apply(reader, key).ifPresent(loadConsumer)));
        return this;
    }

    public JsonObject createJson () {
        JsonObject json = new JsonObject();
        for (ConfigEntry<?> entry : this.entries) {
            Object unknownValue = entry.getValue();
            if (unknownValue instanceof String value) json.addProperty(entry.key, value);
            else if (unknownValue instanceof Number value) json.addProperty(entry.key, value);
            else if (unknownValue instanceof Boolean value) json.addProperty(entry.key, value);
            else if (unknownValue instanceof Character value) json.addProperty(entry.key, value);
            else if (unknownValue instanceof JsonElement value) json.add(entry.key, value);
        }

        return json;
    }

    public void loadFromJson (JsonObject json) {
        JsonReader reader = new JsonReader(json);
        this.loadFromJson(reader);
    }

    public void loadFromJson (JsonReader reader) {
        this.entries.forEach(entry -> entry.saveConsumer.accept(reader));
    }

    private record ConfigEntry<T> (String key, Supplier<T> valueSupplier, Consumer<JsonReader> saveConsumer) {
        public T getValue () {
            return this.valueSupplier.get();
        }
    }
}
