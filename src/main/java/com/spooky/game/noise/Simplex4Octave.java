package com.spooky.game.noise;

public class Simplex4Octave implements IGreyscaleGenerator2D {

    private FastNoiseLite noise;

    private Simplex4Octave(int seed) {
        noise = new FastNoiseLite();
        noise.SetSeed(seed);
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.SetFrequency(0.005f);
        noise.SetFractalType(FastNoiseLite.FractalType.FBm);
        noise.SetFractalOctaves(4);
    }

    public static Simplex4Octave fromRandomSeed() {
        return new Simplex4Octave((int)(Math.random() * Integer.MAX_VALUE));
    }

    public static Simplex4Octave fromSeed(int seed) {
        return new Simplex4Octave(seed);
    }

    @Override
    public float getValue(float x, float y) {
        return noise.GetNoise(x, y);
    }
}
