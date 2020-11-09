package com.spooky.game;

import com.spooky.engine.Transform;
import com.spooky.engine.flow.Window;
import com.spooky.engine.graphics.BatchPixelRenderer;
import com.spooky.engine.graphics.Color;
import com.spooky.engine.graphics.Pixel;
import com.spooky.engine.util.Camera;
import com.spooky.engine.flow.Scene;
import com.spooky.game.noise.FastNoiseLite;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Example scene class
 */
public class ExampleScene extends Scene {

    private final int NUM_PIXELS = Window.getWidth() * Window.getHeight() / (Pixel.PIXEL_SIZE * Pixel.PIXEL_SIZE);
    private BatchPixelRenderer batchPixelRenderer;

    @Override
    public void update(float deltaTime) {
        batchPixelRenderer.render(camera);
    }

    @Override
    public void init() {
        System.out.println(NUM_PIXELS);

        FastNoiseLite noise = new FastNoiseLite();
        noise.SetSeed((int)(Math.random() * Integer.MAX_VALUE));
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.SetFrequency(0.005f);
        noise.SetFractalType(FastNoiseLite.FractalType.FBm);
        noise.SetFractalOctaves(4);

        camera = new Camera(new Vector2f());
        batchPixelRenderer = new BatchPixelRenderer(NUM_PIXELS);
        for (int i = 0; i < Window.getWidth(); i += Pixel.PIXEL_SIZE) {
            for (int j = 0; j < Window.getHeight(); j += Pixel.PIXEL_SIZE) {
                float grey = (noise.GetNoise(i, j) + 1) * 128;
                Pixel p = new Pixel(new Transform(i, j), Color.grey((int)grey));
                batchPixelRenderer.addPixel(p);
            }
        }
        batchPixelRenderer.start();
    }
}
