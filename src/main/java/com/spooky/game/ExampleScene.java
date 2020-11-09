package com.spooky.game;

import com.spooky.engine.flow.Window;
import com.spooky.engine.graphics.ChunkRenderer;
import com.spooky.engine.graphics.Color;
import com.spooky.engine.graphics.Block;
import com.spooky.engine.util.Camera;
import com.spooky.engine.flow.Scene;
import com.spooky.game.noise.FastNoiseLite;
import com.spooky.game.noise.Simplex4Octave;
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
        chunk.generate(Simplex4Octave.fromRandomSeed());
        chunkRenderer = new ChunkRenderer(chunk);
        chunkRenderer.start();
    }
}
