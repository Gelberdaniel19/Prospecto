package com.spooky.game.noise;

/**
 * Add two generators together to get the combination of them
 */
public class Add implements IGreyscaleGenerator2D {

    private IGreyscaleGenerator2D gen1, gen2;

    private Add(IGreyscaleGenerator2D gen1, IGreyscaleGenerator2D gen2) {
        this.gen1 = gen1;
        this.gen2 = gen2;
    }

    public static Add build(IGreyscaleGenerator2D gen1, IGreyscaleGenerator2D gen2) {
        return new Add(gen1, gen2);
    }

    @Override
    public float getValue(float x, float y) {
        float val = gen1.getValue(x, y) * gen2.getValue(x, y);
        return val;
    }
}
