package com.spooky.game;

import com.spooky.engine.flow.Window;
import com.spooky.engine.graphics.ChunkRenderer;
import com.spooky.engine.graphics.Color;
import com.spooky.engine.graphics.Block;
import com.spooky.engine.util.Camera;
import com.spooky.engine.flow.Scene;
import com.spooky.game.noise.FastNoiseLite;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

/**
 * Example scene class
 */
public class ExampleScene extends Scene {

    private ChunkRenderer chunkRenderer;
    private Chunk chunk;

    @Override
    public void update(float deltaTime) {
        //System.out.println(1 / deltaTime);
        chunkRenderer.render(camera);
    }

    @Override
    public void init() {
        System.out.println("Blocks in chunk: " + (Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE));
        camera = new Camera(new Vector2f());
        chunk = new Chunk(1, 1);

        FastNoiseLite noise = new FastNoiseLite();
        noise.SetSeed((int)(Math.random() * Integer.MAX_VALUE));
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.SetFrequency(0.005f);
        noise.SetFractalType(FastNoiseLite.FractalType.FBm);
        noise.SetFractalOctaves(4);

        for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
            for (int j = 0; j < Chunk.CHUNK_SIZE; j++) {
                int grey = (int)(noise.GetNoise(i + 128, j + 128) * 128 + 128);
                chunk.setPixel(i, j, new Block(new Vector2i(i + 128, j + 128), Color.grey(grey)));
            }
        }

        chunkRenderer = new ChunkRenderer(chunk);
        chunkRenderer.start();
    }
}
