/*
 * Copyright (C) 2026 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.util.json;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * Convenient builder class for creating JsonArray objects and converting them to strings. This is a wrapper of JsonArray.
 */
public class JsonArrayBuilder {
    private final JsonArray array;

    public JsonArrayBuilder () {
        this.array = new JsonArray();
    }

    /**
     * Adds a value to the json array.
     * @param value The value to add.
     * @return this
     */
    public JsonArrayBuilder append (String value) {
        this.array.add(value);
        return this;
    }

    /**
     * Adds a value to the json array.
     * @param value The value to add.
     * @return this
     */
    public JsonArrayBuilder append (boolean value) {
        this.array.add(value);
        return this;
    }

    /**
     * Adds a value to the json array.
     * @param value The value to add.
     * @return this
     */
    public JsonArrayBuilder append (Number value) {
        this.array.add(value);
        return this;
    }

    /**
     * Adds a value to the json array.
     * @param value The value to add.
     * @return this
     */
    public JsonArrayBuilder append (char value) {
        this.array.add(value);
        return this;
    }

    /**
     * Adds a value to the json array.
     * @param value The value to add.
     * @return this
     */
    public JsonArrayBuilder append (JsonElement value) {
        this.array.add(value);
        return this;
    }

    /**
     * Adds a value to the json array.
     * @param builder The builder representing the json element to add.
     * @return this
     */
    public JsonArrayBuilder append (JsonArrayBuilder builder) {
        this.array.add(builder.getJson());
        return this;
    }

    /**
     * Adds a value to the json array.
     * @param builder The builder representing the json element to add.
     * @return this
     */
    public JsonArrayBuilder append (JsonBuilder builder) {
        this.array.add(builder.getJson());
        return this;
    }

    /**
     * @return The internal json held by this builder.
     */
    public JsonArray getJson () {
        return this.array;
    }

    @Override
    public String toString () {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this.array);
    }
}
