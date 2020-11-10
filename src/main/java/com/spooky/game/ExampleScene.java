package com.spooky.game;

import com.spooky.engine.Camera;
import com.spooky.engine.Scene;
import com.spooky.engine.KeyListener;
import com.spooky.game.chunk.Chunk;
import com.spooky.game.chunk.ChunkLoader;
import com.spooky.game.noise.*;
import org.joml.Vector2f;

import java.awt.event.KeyEvent;

/**
 * Example scene class
 */
public class ExampleScene extends Scene {

    private ChunkLoader chunkLoader;

    private final float PAN_SPEED = 200;

    @Override
    public void update(float deltaTime) {
        if (Math.random() > 0.95) System.out.println(1/deltaTime);

        // Camera panning
        if (KeyListener.isKeyPressed(KeyEvent.VK_A)) camera.move(new Vector2f(-PAN_SPEED * deltaTime, 0.0f));
        if (KeyListener.isKeyPressed(KeyEvent.VK_D)) camera.move(new Vector2f(PAN_SPEED * deltaTime, 0.0f));
        if (KeyListener.isKeyPressed(KeyEvent.VK_S)) camera.move(new Vector2f(0.0f, -PAN_SPEED * deltaTime));
        if (KeyListener.isKeyPressed(KeyEvent.VK_W)) camera.move(new Vector2f(0.0f, PAN_SPEED * deltaTime));

        chunkLoader.update(camera.getPosition());
        chunkLoader.render();
    }

    @Override
    public void init() {
        System.out.println("Blocks in chunk: " + (Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE));
        camera = new Camera(new Vector2f());
//        IGreyscaleGenerator2D generator =
//                Sharpen.build(
//                        Add.build(
//                                Multiply.build(
//                                        Remap.build(Simplex.buildRandom().withFrequency(0.01f))
//                                                .withFromMin(0.92f)
//                                                .withToMin(1f)
//                                                .withToMax(2.3f),
//                                        Simplex.buildRandom().withFrequency(0.02f)
//                                                .withFractal(10)
//                                ),
//                                Simplex.buildRandom().withFrequency(0.02f).withFractal(10)
//                        )
//                )
//                .withIterations(2);
//
//        generator = Remap.build(generator)
//                .withFromMax(-0.5f);
//
//        generator = Multiply.build(
//                PositionOutput.build().withYMult(-30),
//                generator
//        );
//
//        generator = Combine.build(
//                generator,
//                Multiply.build(
//                        Simplex.buildRandom().withFrequency(0.01f).withFractal(6),
//                        PositionOutput.build().withYMult(-15).withYOff(-10)
//                )
//        );
//
//        generator = DomainScale.build(generator, 0.03f);
//        generator = Clampify.build(generator).withThreshold(-0.3f);

        chunkLoader = new ChunkLoader(2, 1, Simplex.buildRandom(), camera);
        chunkLoader.update(camera.getPosition());
    }
}
