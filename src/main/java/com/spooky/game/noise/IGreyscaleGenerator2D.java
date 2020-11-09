package com.spooky.game.noise;

public interface IGreyscaleGenerator2D {
    /**
     * Generate a value from the 2D coordinates.
     * @param x coordinate.
     * @param y coordinate.
     * @return a value between -1 and 1.
     */
    float getValue(float x, float y);
}
