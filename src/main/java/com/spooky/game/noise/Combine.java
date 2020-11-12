package com.spooky.game.noise;

import com.spooky.engine.MathUtils;

/**
 * Combine two generators together: each pixel will be at least as bright as previous pixel
 */
public class Combine implements IGreyscaleGenerator2D {

    private final IGreyscaleGenerator2D gen1;
    private final IGreyscaleGenerator2D gen2;

    private Combine(IGreyscaleGenerator2D gen1, IGreyscaleGenerator2D gen2) {
        this.gen1 = gen1;
        this.gen2 = gen2;
    }

    public static Combine build(IGreyscaleGenerator2D gen1, IGreyscaleGenerator2D gen2) {
        return new Combine(gen1, gen2);
    }

    @Override
    public float getValue(float x, float y) {
        float val1 = gen1.getValue(x, y) / 2 + 0.5f;
        float val2 = gen2.getValue(x, y) / 2 + 0.5f;
        float sum = (val1 + val2) * 2 - 1;
        return MathUtils.clamp(sum, -1, 1);
    }
}
