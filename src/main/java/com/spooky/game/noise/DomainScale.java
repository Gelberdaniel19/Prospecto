package com.spooky.game.noise;

/**
 * Scales a generator by some amount.
 */
public class DomainScale implements IGreyscaleGenerator2D {

    private final IGreyscaleGenerator2D generator;
    float scale = 1f;

    private DomainScale(IGreyscaleGenerator2D generator, float scale) {
        this.generator = generator;
        this.scale = scale;
    }

    public static DomainScale build(IGreyscaleGenerator2D generator, float scale) {
        return new DomainScale(generator, scale);
    }

    @Override
    public float getValue(float x, float y) {
        return generator.getValue(x * scale, y * scale);
    }
}
