/*
 * Copyright (C) 2024 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.util;

import java.util.Iterator;

/**
 * Builds JSON-formatted strings. This is a wrapper of the StringBuilder type.
 * <p>
 * Deprecated, this older version is only present for compatibility purposes. Use the other one instead.
 */
@Deprecated(forRemoval = true)
public class JsonBuilder {
    private final StringBuilder builder;
    private int indentationLevel;

    public JsonBuilder () {
        this.builder = new StringBuilder();
        this.indentationLevel = 0;
    }

    /**
     * Starts the JSON object.
     * @return this
     */
    public JsonBuilder start () {
        this.builder.append("{\n");
        this.indentationLevel++;
        return this;
    }

    /**
     * Adds a single string value to the JSON object.
     * @param value The string value.
     * @return this
     */
    public JsonBuilder append (String value) {
        this.indent();
        this.builder.append("\"").append(value).append("\"");
        return this;
    }

    /**
     * Adds a key-value pair to the JSON object.
     * @param key
     * @param value
     * @return this
     */
    public JsonBuilder append (String key, String value) {
        this.indent();
        this.builder.append("\"").append(key).append("\": ");
        this.builder.append("\"").append(value).append("\"");
        return this;
    }

    /**
     * Adds a key-value pair to the JSON object.
     * @param key
     * @param value
     * @return this
     */
    public JsonBuilder append (String key, int value) {
        this.indent();
        this.builder.append("\"").append(key).append("\": ").append(value);
        return this;
    }

    /**
     * Adds a key-value pair to the JSON object.
     * @param key
     * @param value
     * @return this
     */
    public JsonBuilder append (String key, boolean value) {
        this.indent();
        this.builder.append("\"").append(key).append("\": ").append(value);
        return this;
    }

    /**
     * Adds a key-value pair to the JSON object.
     * Note that doubles and floats are indistinguishable after appending.
     * @param key
     * @param value
     * @return this
     */
    public JsonBuilder append (String key, float value) {
        this.indent();
        this.builder.append("\"").append(key).append("\": ").append(value);
        return this;
    }

    /**
     * Adds a key-value pair to the JSON object.
     * Note that doubles and floats are indistinguishable after appending.
     * @param key
     * @param value
     * @return this
     */
    public JsonBuilder append (String key, double value) {
        this.indent();
        this.builder.append("\"").append(key).append("\": ").append(value);
        return this;
    }

    /**
     * Closes the current object. This should also be used to close the top-level object.
     * @return this
     */
    public JsonBuilder closeObject () {
        this.indentationLevel--;
        this.indent();
        this.builder.append("}");
        return this;
    }

    /**
     * Creates a key-value pair for a new array under the given key.
     * @param key
     * @return this
     */
    public JsonBuilder startArray (String key) {
        this.indent();
        this.builder.append("\"").append(key).append("\": [");
        this.indentationLevel++;
        return this;
    }

    /**
     * Closes the current array.
     * @return this
     */
    public JsonBuilder closeArray () {
        this.indentationLevel--;
        this.indent();
        this.builder.append("]");
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
    public JsonBuilder createArray (String key, Iterable<String> values) {
        this.startArray(key).newLine(false);

        Iterator<String> iter = values.iterator();

        if (iter.hasNext()) {
            String last = iter.next();
            
            while (iter.hasNext()) {
                this.append(last).newLine(true);
                last = iter.next();
            }

            this.append(last);
        }

        this.newLine(false);
        this.closeArray();
        return this;
    }

    /**
     * Creates an entire array from an undefined number of String arguments.
     * 
     * Uses the iterable variant as its backend.
     * 
     * @param key
     * @param values
     * @return this
     */
    public JsonBuilder createArray (String key, String... values) {
        return this.createArray(key, values);
    }

    /**
     * Creates a new line with a comma.
     * @return this
     */
    public JsonBuilder newLine () {
        return this.newLine(true);
    }

    /**
     * Creates a new line with an optional comma.
     * @param addComma Whether a comma should be added at the end of the line.
     * @return this
     */
    public JsonBuilder newLine (boolean addComma) {
        if (addComma) this.builder.append(",");
        this.builder.append("\n");
        return this;
    }

    private void indent () {
        for (int i = 0; i < this.indentationLevel; ++i) {
            this.builder.append("\t");
        }
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
        return this.builder.toString();
    }
}
