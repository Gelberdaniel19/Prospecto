package com.spooky.game;

import com.spooky.engine.Camera;
import com.spooky.engine.KeyListener;
import com.spooky.engine.Scene;
import com.spooky.game.chunk.Chunk;
import com.spooky.game.chunk.ChunkLoader;
import com.spooky.game.chunk.ChunkPool;
import com.spooky.game.noise.*;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.awt.event.KeyEvent;
import java.util.Timer;

/**
 * Example scene class
 * <p>
 * USE THIS REFERENCE FOR RANDOM GENERATION
 * http://accidentalnoise.sourceforge.net/minecraftworlds.html
 */
public class ExampleScene extends Scene {

    private final float PAN_SPEED = 200;
    private ChunkLoader chunkLoader;
    private ChunkPool chunkPool;

    @Override
    public void update(float deltaTime) {
        //if (Math.random() > 0.95) System.out.println(1/deltaTime);

        // Camera panning
        if (KeyListener.isKeyPressed(KeyEvent.VK_A)) camera.move(new Vector2f(-PAN_SPEED * deltaTime, 0.0f));
        if (KeyListener.isKeyPressed(KeyEvent.VK_D)) camera.move(new Vector2f(PAN_SPEED * deltaTime, 0.0f));
        if (KeyListener.isKeyPressed(KeyEvent.VK_S)) camera.move(new Vector2f(0.0f, -PAN_SPEED * deltaTime));
        if (KeyListener.isKeyPressed(KeyEvent.VK_W)) camera.move(new Vector2f(0.0f, PAN_SPEED * deltaTime));

        // Logging
        if (KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) System.out.println(chunkLoader);

        chunkPool.setCameraPosition(camera.getPositionCopy());
        chunkLoader.update(camera.getPosition());
        chunkLoader.render();
    }

    @Override
    public void init() {
        System.out.println("Blocks in chunk: " + (Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE));
        camera = new Camera(new Vector2f());
        IBooleanGenerator2D mapGenerator = Clampify.build(Simplex.buildRandom()).withThreshold(-0.3f);

        // Create and start the chunk pool
        chunkPool = new ChunkPool(new Vector2i(3, 2), 50, mapGenerator, camera.getPositionCopy());
        chunkPool.update();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(chunkPool, 0, 500);

        // Create the chunk loader
        chunkLoader = new ChunkLoader(new Vector2i(2, 1), camera, chunkPool);
    }
}
