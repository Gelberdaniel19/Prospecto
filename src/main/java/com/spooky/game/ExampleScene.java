package com.spooky.game;

import com.spooky.engine.Color;
import com.spooky.game.chunk.Block;
import com.spooky.engine.Camera;
import com.spooky.engine.Scene;
import com.spooky.engine.KeyListener;
import com.spooky.game.chunk.Chunk;
import com.spooky.game.chunk.RenderableChunk;
import com.spooky.game.noise.Simplex4Octave;
import org.joml.Vector2f;

import java.awt.event.KeyEvent;

/**
 * Example scene class
 */
public class ExampleScene extends Scene {

    private RenderableChunk chunk;

    private final float PAN_SPEED = 50;

    @Override
    public void update(float deltaTime) {
        // Camera panning
        if (KeyListener.isKeyPressed(KeyEvent.VK_A)) camera.move(new Vector2f(-PAN_SPEED * deltaTime, 0.0f));
        if (KeyListener.isKeyPressed(KeyEvent.VK_D)) camera.move(new Vector2f(PAN_SPEED * deltaTime, 0.0f));
        if (KeyListener.isKeyPressed(KeyEvent.VK_S)) camera.move(new Vector2f(0.0f, -PAN_SPEED * deltaTime));
        if (KeyListener.isKeyPressed(KeyEvent.VK_W)) camera.move(new Vector2f(0.0f, PAN_SPEED * deltaTime));

        // Render the chunk
        chunk.render();
    }

    @Override
    public void init() {
        System.out.println("Blocks in chunk: " + (Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE));
        camera = new Camera(new Vector2f());
        chunk = new RenderableChunk(0, 0, camera);
        chunk.generate(Simplex4Octave.fromRandomSeed());
    }
}
