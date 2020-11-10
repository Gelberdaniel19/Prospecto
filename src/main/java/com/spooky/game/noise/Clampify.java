package com.spooky.game.noise;

/**
 * Returns a value of either 1 or -1 based on the threshold
 */
public class Clampify implements IGreyscaleGenerator2D {

    private final IGreyscaleGenerator2D generator;
    private float threshold = 0;

    public Clampify(IGreyscaleGenerator2D generator) {
        this.generator = generator;
    }

    public Clampify withThreshold(float threshold) {
        this.threshold = threshold;
        return this;
    }

    public static Clampify build(IGreyscaleGenerator2D generator) {
        return new Clampify(generator);
    }

    @Override
    public float getValue(float x, float y) {
        float val = generator.getValue(x, y);
        return val > threshold ? 1 : -1;
    }

}
