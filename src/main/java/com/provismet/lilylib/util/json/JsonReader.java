/*
 * Copyright (C) 2025 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.util.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Optional;
import java.util.function.Function;

public class JsonReader {
    private final JsonObject json;

    public JsonReader (JsonObject json) {
        this.json = json;
    }

    @Nullable
    public static JsonReader file (File file) throws FileNotFoundException {
        JsonElement element = JsonParser.parseReader(new FileReader(file));
        if (element instanceof JsonObject json) {
            return new JsonReader(json);
        }
        return null;
    }

    public Optional<Integer> getInteger (String key) {
        return this.getGeneric(key, JsonPrimitive::getAsInt);
    }

    public Optional<Double> getDouble (String key) {
        return this.getGeneric(key, JsonPrimitive::getAsDouble);
    }

    public Optional<Float> getFloat (String key) {
        return this.getGeneric(key, JsonPrimitive::getAsFloat);
    }

    public Optional<Short> getShort (String key) {
        return this.getGeneric(key, JsonPrimitive::getAsShort);
    }

    public Optional<Long> getLong (String key) {
        return this.getGeneric(key, JsonPrimitive::getAsLong);
    }

    public Optional<Byte> getByte (String key) {
        return this.getGeneric(key, JsonPrimitive::getAsByte);
    }

    public Optional<Boolean> getBoolean (String key) {
        return this.getGeneric(key, JsonPrimitive::getAsBoolean);
    }

    public Optional<String> getString (String key) {
        return this.getGeneric(key, JsonPrimitive::getAsString);
    }

    public Optional<JsonArray> getArray (String key) {
        return this.get(key).map(element -> {
            if (element instanceof JsonArray array) return array;
            return null;
        });
    }

    public Optional<JsonPrimitive> getPrimitive (String key) {
        return this.get(key).map(element -> {
            if (element instanceof JsonPrimitive primitive) return primitive;
            return null;
        });
    }

    public Optional<JsonElement> get (String key) {
        if (this.json.has(key)) {
            return Optional.of(this.json.get(key));
        }
        return Optional.empty();
    }

    public <T> Optional<T> getGeneric (String key, Function<JsonPrimitive, T> function) {
        try {
            return this.getPrimitive(key).map(function);
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }
}
