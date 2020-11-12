package com.spooky.game.noise;

public interface IBooleanGenerator2D {
    /**
     * Generate a value from the 2D coordinates.
     *
     * @param x coordinate.
     * @param y coordinate.
     * @return a true/false value.
     */
    boolean getValue(float x, float y);
}
