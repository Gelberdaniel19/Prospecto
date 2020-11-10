package com.spooky.game.noise;

public class Sharpen implements IGreyscaleGenerator2D {

    private IGreyscaleGenerator2D generator;
    private int iterations;

    public Sharpen(IGreyscaleGenerator2D generator) {
        this.generator = generator;
    }

    public static Sharpen build(IGreyscaleGenerator2D generator) {
        return new Sharpen(generator);
    }

    public Sharpen withIterations(int iterations) {
        this.iterations = iterations;
        return this;
    }

    @Override
    public float getValue(float x, float y) {
        // [0, 2]
        float val = generator.getValue(x, y) + 1;

        // [0, 2^iterations]
        for (int i = 0; i < iterations; i++) {
            val *= val;
        }

        // [0, 2]
        for (int i = 0; i < iterations; i++) {
            val /= 2;
        }

        // [-1, 1]
        return val / 2 - 1;
    }
}
