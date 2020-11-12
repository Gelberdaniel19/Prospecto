package com.spooky.game;

import com.spooky.engine.Camera;
import com.spooky.game.chunk.ChunkLoader;
import com.spooky.game.chunk.ChunkPool;
import com.spooky.game.chunk.ChunkUtils;
import com.spooky.game.noise.IBooleanGenerator2D;
import org.joml.Vector2i;

import java.util.Timer;

/**
 * A game object which contains the blocks of the world.
 * Handles chunk loading and pooling.
 */
public class World {

    private final ChunkLoader chunkLoader;
    private final ChunkPool chunkPool;
    private final Camera camera;
    private final Timer timer;

    public World(IBooleanGenerator2D mapGenerator, Camera camera) {
        this.camera = camera;

        // Create and start the chunk pool
        Vector2i onScr = ChunkUtils.getChunksOnScreen();
        chunkPool = new ChunkPool(new Vector2i(onScr.x / 2 + 1, onScr.y / 2 + 1), 25, mapGenerator, camera.getPositionCopy());
        chunkPool.update();
        timer = new Timer();
        timer.scheduleAtFixedRate(chunkPool, 0, 500);

        // Create the chunk loader
        chunkLoader = new ChunkLoader(new Vector2i(onScr.x / 2, onScr.y / 2), camera, chunkPool);
    }

    public void update() {
        chunkPool.setCameraPosition(camera.getPositionCopy());
        chunkLoader.update(camera.getPosition());
    }

    public void render() {
        chunkLoader.render();
    }

    /**
     * Stop the chunk pool.
     */
    public void stop() {
        timer.cancel();
        timer.purge();
    }

}
