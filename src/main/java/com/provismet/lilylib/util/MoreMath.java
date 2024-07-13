/*
 * Copyright (C) 2024 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * An additional Math-related class that contains functions not covered by MathHelper.
 */
public class MoreMath {
    public static double roundDownToMultiple (double value, double denominator) {
        return (double)MathHelper.floor(value / denominator) * denominator;
    }

    public static float roundDownToMultipleFloat (float value, float denominator) {
        return (float)MathHelper.floor(value / denominator) * denominator;
    }

    /**
     * Allows for the creation and representation of right-angled triangles.
     * 
     *             end
     *           /|
     *          / |
     *       h /  | a
     *        /   |
     *       /    |
     *      /_____|
     * start    o
     */
    public static class RightAngledTriangle {
        private final double hypotenuseLength; // Defined as the line between the start and end vectors.
        private final double oppositeLength; // The side opposite to the end vector.
        private final double adjacentLength; // The non-hypotenuse side adjacent to the end vector.

        public RightAngledTriangle (Vec3d hypotenuseStart, Vec3d hypotenuseEnd) {
            this.oppositeLength = clamped(hypotenuseEnd.getX() - hypotenuseStart.getX());
            this.adjacentLength = clamped(hypotenuseEnd.getZ() - hypotenuseStart.getZ());
            this.hypotenuseLength = Math.sqrt(this.oppositeLength * this.oppositeLength + this.adjacentLength * this.adjacentLength);
        }

        private double clamped (double value) {
            if (value == 0) return 0.00001;
            else return value;
        }

        public double cosine () {
            return this.adjacentLength / this.hypotenuseLength;
        }

        public double sine () {
            return this.oppositeLength / this.hypotenuseLength;
        }

        public double tangent () {
            return sine() / cosine();
        }
    }
}
