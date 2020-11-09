package com.spooky.game;

import com.spooky.engine.flow.Window;
import com.spooky.engine.graphics.ChunkRenderer;
import com.spooky.engine.graphics.Color;
import com.spooky.engine.graphics.Block;
import com.spooky.engine.util.Camera;
import com.spooky.engine.flow.Scene;
import com.spooky.engine.util.KeyListener;
import com.spooky.game.noise.FastNoiseLite;
import com.spooky.game.noise.Simplex4Octave;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Example scene class
 */
public class ExampleScene extends Scene {

    private ChunkRenderer chunkRenderer;
    private Chunk chunk;

    private final float PAN_SPEED = 50;

    @Override
    public void update(float deltaTime) {
        //System.out.println(1 / deltaTime);
        if (KeyListener.isKeyPressed(KeyEvent.VK_A)) {
            camera.move(new Vector2f(-PAN_SPEED * deltaTime, 0.0f));
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_D)) {
            camera.move(new Vector2f(PAN_SPEED * deltaTime, 0.0f));
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_S)) {
            camera.move(new Vector2f(0.0f, -PAN_SPEED * deltaTime));
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_W)) {
            camera.move(new Vector2f(0.0f, PAN_SPEED * deltaTime));
        }
        if (KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
                for (int j = 0; j < Chunk.CHUNK_SIZE; j++) {
                    Block block = chunk.getPixel(i, j);
                    chunk.setPixelColor(i, j, new Color(block.color.r + 1, block.color.g, block.color.b));
                }
            }
        }
        chunkRenderer.render(camera);
    }

    @Override
    public void init() {
        System.out.println("Blocks in chunk: " + (Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE));
        camera = new Camera(new Vector2f());
        chunk = new Chunk(1, 1);
        chunk.generate(Simplex4Octave.fromRandomSeed());
        chunkRenderer = new ChunkRenderer(chunk);
        chunkRenderer.start();
    }
}
