package com.spooky.engine;

public class MathUtils {

    public static float clamp(float x, float min, float max) {
        if (x < min) return min;
        if (x > max) return max;
        return x;
    }

}
