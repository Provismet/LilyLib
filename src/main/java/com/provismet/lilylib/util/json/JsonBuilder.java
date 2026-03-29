/*
 * Copyright (C) 2024-2026 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.util.json;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;

/**
 * Builds JSON-formatted strings. This is a wrapper of JsonObject.
 */
public class JsonBuilder {
    private final JsonObject json = new JsonObject();

    public JsonBuilder () {
    }

    /**
     * Adds a key-value pair to the JSON object.
     *
     * @param key Key
     * @param value Value
     * @return this
     */
    public JsonBuilder append (String key, String value) {
        this.json.addProperty(key, value);
        return this;
    }

    /**
     * Adds a key-value pair to the JSON object.
     *
     * @param key Key
     * @param value Value
     * @return this
     */
    public JsonBuilder append (String key, Number value) {
        this.json.addProperty(key, value);
        return this;
    }

    /**
     * Adds a key-value pair to the JSON object.
     *
     * @param key Key
     * @param value Value
     * @return this
     */
    public JsonBuilder append (String key, boolean value) {
        this.json.addProperty(key, value);
        return this;
    }

    /**
     * Adds a key-value pair to the JSON object.
     *
     * @param key Key
     * @param value Value
     * @return this
     */
    public JsonBuilder append (String key, JsonBuilder value) {
        this.json.add(key, value.json);
        return this;
    }

    /**
     * Adds a key-value pair to the JSON object.
     *
     * @param key Key
     * @param value Value
     * @return this
     */
    public JsonBuilder append (String key, JsonElement value) {
        this.json.add(key, value);
        return this;
    }

    /**
     * <p> Creates an entire array from an iterable of String values. </p>
     * 
     * <p> This function encapsulates the starting and closing of the array. </p>
     * 
     * @param key The key of the key-value pair.
     * @param values The values of the array.
     * @return this
     */
    public JsonBuilder appendArray (String key, Iterable<String> values) {
        JsonArray array = new JsonArray();
        for (String value : values) {
            array.add(value);
        }
        this.json.add(key, array);
        return this;
    }

    /**
     * <p> Creates an entire array from an undefined number of String arguments. </p>
     * 
     * <p> Uses the iterable variant as its implementation. </p>
     * 
     * @param key The key of the array.
     * @param values The contents of the array.
     * @return this
     */
    public JsonBuilder appendArray (String key, String... values) {
        return this.appendArray(key, Arrays.stream(values).toList());
    }

    /**
     * <p> Builds and returns the current String held by the internal StringBuilder. </p>
     * 
     * <p> Use this to output the final JSON string. </p>
     * 
     * @return The current string representation of the JSON object.
     */
    @Override
    public String toString () {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this.json);
    }

    /**
     * @return The internal json held by this builder.
     */
    public JsonObject getJson () {
        return this.json;
    }
}
