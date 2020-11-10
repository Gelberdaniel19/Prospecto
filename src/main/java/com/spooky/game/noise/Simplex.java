package com.spooky.game.noise;

public class Simplex implements IGreyscaleGenerator2D {

    private FastNoiseLite noise;

    private Simplex(int seed) {
        noise = new FastNoiseLite();
        noise.SetSeed(seed);
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.SetFrequency(0.005f);
        noise.SetFractalType(FastNoiseLite.FractalType.FBm);
        noise.SetFractalOctaves(1);
    }

    public Simplex withNoiseType(FastNoiseLite.NoiseType noiseType) {
        noise.SetNoiseType(noiseType);
        return this;
    }

    public Simplex withFrequency(float frequency) {
        noise.SetFrequency(frequency);
        return this;
    }

    public Simplex withFractal(FastNoiseLite.FractalType type, int octaves) {
        noise.SetFractalType(type);
        noise.SetFractalOctaves(octaves);
        return this;
    }

    public Simplex withFractal(int octaves) {
        noise.SetFractalOctaves(octaves);
        return this;
    }

    public static Simplex buildRandom() {
        return new Simplex((int)(Math.random() * Integer.MAX_VALUE));
    }

    public static Simplex buildWithSeed(int seed) {
        return new Simplex(seed);
    }

    @Override
    public float getValue(float x, float y) {
        return noise.GetNoise(x, y);
    }
}
