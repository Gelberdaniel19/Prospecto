package com.spooky.game.noise;

/**
 * Add two generators together to get the combination of them
 */
public class Multiply implements IGreyscaleGenerator2D {

    private IGreyscaleGenerator2D gen1, gen2;

    private Multiply(IGreyscaleGenerator2D gen1, IGreyscaleGenerator2D gen2) {
        this.gen1 = gen1;
        this.gen2 = gen2;
    }

    public static Multiply build(IGreyscaleGenerator2D gen1, IGreyscaleGenerator2D gen2) {
        return new Multiply(gen1, gen2);
    }

    @Override
    public float getValue(float x, float y) {
        float val1 = gen1.getValue(x, y) / 2.0f + 0.5f;
        float val2 = gen2.getValue(x, y) / 2.0f + 0.5f;
        float res0to1 = val1 * val2;
        return res0to1 * 2 - 1;
    }
}
