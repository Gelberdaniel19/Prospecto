package com.spooky.game;

import com.spooky.engine.Transform;
import com.spooky.engine.flow.Window;
import com.spooky.engine.graphics.BatchPixelRenderer;
import com.spooky.engine.graphics.Color;
import com.spooky.engine.graphics.Pixel;
import com.spooky.engine.util.Camera;
import com.spooky.engine.Shader;
import com.spooky.engine.flow.Scene;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexArrayObject;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
        camera = new Camera(new Vector2f());
        batchPixelRenderer = new BatchPixelRenderer(NUM_PIXELS);
        for (int i = 0; i < Window.getWidth(); i += Pixel.PIXEL_SIZE) {
            for (int j = 0; j < Window.getHeight(); j += Pixel.PIXEL_SIZE) {
                batchPixelRenderer.addPixel(new Pixel(new Transform(i, j), Color.random()));
            }
        }
        batchPixelRenderer.start();
    }
}
