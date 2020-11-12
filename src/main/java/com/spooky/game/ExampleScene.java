package com.spooky.game;

import com.spooky.engine.Camera;
import com.spooky.engine.Scene;
import com.spooky.game.chunk.Chunk;
import com.spooky.game.chunk.ChunkPool;
import com.spooky.game.noise.*;
import org.joml.Vector2f;
import org.joml.Vector2i;

/**
 * Example scene class
 * <p>
 * USE THIS REFERENCE FOR RANDOM GENERATION
 * http://accidentalnoise.sourceforge.net/minecraftworlds.html
 */
public class ExampleScene extends Scene {

//    private ChunkLoader chunkLoader;

    private final float PAN_SPEED = 200;

    @Override
    public void update(float deltaTime) {
        //if (Math.random() > 0.95) System.out.println(1/deltaTime);

//        // Camera panning
//        if (KeyListener.isKeyPressed(KeyEvent.VK_A)) camera.move(new Vector2f(-PAN_SPEED * deltaTime, 0.0f));
//        if (KeyListener.isKeyPressed(KeyEvent.VK_D)) camera.move(new Vector2f(PAN_SPEED * deltaTime, 0.0f));
//        if (KeyListener.isKeyPressed(KeyEvent.VK_S)) camera.move(new Vector2f(0.0f, -PAN_SPEED * deltaTime));
//        if (KeyListener.isKeyPressed(KeyEvent.VK_W)) camera.move(new Vector2f(0.0f, PAN_SPEED * deltaTime));
//
//        if (cooldown <= 0 && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
//            cooldown = 100;
//            System.out.println(chunkLoader);
//        }
//        cooldown -= 1;
//
//        chunkLoader.update(camera.getPosition());
//        chunkLoader.render();
    }

    @Override
    public void init() {
        System.out.println("Blocks in chunk: " + (Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE));
        camera = new Camera(new Vector2f());
        IBooleanGenerator2D mapGenerator = Clampify.build(Simplex.buildRandom()).withThreshold(-0.3f);
//
//        chunkLoader = new ChunkLoader(2, 2, mapGenerator, camera);
//        chunkLoader.update(camera.getPosition());

        ChunkPool pool = new ChunkPool(new Vector2i(2, 2), 50, mapGenerator);
        pool.update(camera.getPosition());
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                System.out.println(pool.get(new Vector2i(i, j)));
            }
        }
    }
}
