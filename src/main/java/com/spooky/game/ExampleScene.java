package com.spooky.game;

import com.spooky.engine.Transform;
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

    private final int NUM_PIXELS = 100000;
    private BatchPixelRenderer batchPixelRenderer;

    @Override
    public void update(float deltaTime) {
        System.out.println(1/deltaTime);
        batchPixelRenderer.render(camera);
    }

    @Override
    public void init() {
        camera = new Camera(new Vector2f());
        batchPixelRenderer = new BatchPixelRenderer(NUM_PIXELS);
        for (int i = 0; i < NUM_PIXELS; i++) {
            batchPixelRenderer.addPixel(new Pixel(new Transform(100, 100), new Color(255, 150, 0)));
        }
        batchPixelRenderer.start();
    }
}
