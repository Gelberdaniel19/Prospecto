package com.spooky.game.noise;

/**
 * Remaps the minimum and maximum to a different scale.
 */
public class Remap implements IGreyscaleGenerator2D {

    private IGreyscaleGenerator2D generator;
    private float fromMin = -1f;
    private float fromMax = 1f;
    private float toMin = -1f;
    private float toMax = 1f;

    public Remap(IGreyscaleGenerator2D generator) {
        this.generator = generator;
    }

    public static Remap build(IGreyscaleGenerator2D generator) {
        return new Remap(generator);
    }

    public Remap withFromMin(float fromMin) {
        this.fromMin = fromMin;
        return this;
    }

    public Remap withFromMax(float fromMax) {
        this.fromMax = fromMax;
        return this;
    }

    public Remap withToMin(float toMin) {
        this.toMin = toMin;
        return this;
    }

    public Remap withToMax(float toMax) {
        this.toMax = toMax;
        return this;
    }

    @Override
    public float getValue(float x, float y) {
        float val = ((generator.getValue(x, y) - fromMin) / (fromMax - fromMin)) * (toMax - toMin) + toMin;
        return Math.min(Math.max(val, -1f), 1f);
    }
}
