package com.spooky.game;

import com.spooky.engine.Camera;
import com.spooky.engine.Scene;
import com.spooky.engine.KeyListener;
import com.spooky.game.chunk.Chunk;
import com.spooky.game.chunk.ChunkLoader;
import com.spooky.game.noise.Simplex4Octave;
import org.joml.Vector2f;

import java.awt.event.KeyEvent;

/**
 * Example scene class
 */
public class ExampleScene extends Scene {

    private ChunkLoader chunkLoader;

    private final float PAN_SPEED = 50;

    @Override
    public void update(float deltaTime) {
        // Camera panning
        if (KeyListener.isKeyPressed(KeyEvent.VK_A)) camera.move(new Vector2f(-PAN_SPEED * deltaTime, 0.0f));
        if (KeyListener.isKeyPressed(KeyEvent.VK_D)) camera.move(new Vector2f(PAN_SPEED * deltaTime, 0.0f));
        if (KeyListener.isKeyPressed(KeyEvent.VK_S)) camera.move(new Vector2f(0.0f, -PAN_SPEED * deltaTime));
        if (KeyListener.isKeyPressed(KeyEvent.VK_W)) camera.move(new Vector2f(0.0f, PAN_SPEED * deltaTime));

        // Update
        chunkLoader.update(camera.getPosition());

        // Render the chunk
        chunkLoader.render();
    }

    @Override
    public void init() {
        System.out.println("Blocks in chunk: " + (Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE));
        camera = new Camera(new Vector2f());
        chunkLoader = new ChunkLoader(2, 1, Simplex4Octave.fromRandomSeed(), camera);
        chunkLoader.update(camera.getPosition());
    }
}
