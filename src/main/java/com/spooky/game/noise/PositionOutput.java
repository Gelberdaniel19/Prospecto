package com.spooky.game.noise;

import com.spooky.engine.MathUtils;

/**
 *
 */
public class PositionOutput implements IGreyscaleGenerator2D {

    private float yMultiplier = 0;
    private float yOffset = 0;

    public PositionOutput withYMult(float yMultiplier) {
        this.yMultiplier = yMultiplier;
        return this;
    }

    public PositionOutput withYOff(float yOffset) {
        this.yOffset = yOffset;
        return this;
    }

    public static PositionOutput build() {
        return new PositionOutput();
    }

    @Override
    public float getValue(float x, float y) {
        return MathUtils.clamp((y - yOffset) / yMultiplier, -1, 1);
    }
}
