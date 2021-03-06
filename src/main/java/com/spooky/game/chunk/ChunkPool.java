package com.spooky.game.chunk;

import com.spooky.game.noise.IBooleanGenerator2D;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * A thread safe class that contains no display functions. This class,
 * if updated often enough, will ensure that any chunks within the
 * poolDistance will be loaded, including vertex data.
 */
public class ChunkPool extends TimerTask {

    private final List<ChunkView> chunkQueue;
    private final Vector2i poolDistance;
    private final int cacheSize;
    private final IBooleanGenerator2D generator;
    private Vector2f cameraPosition;

    /**
     * Pool distance should be at least one larger than render distance
     *
     * @param poolDistance   how many chunks to each side o the player to make
     *                       sure exist in the queue.
     * @param additionalCacheSize   how many additional chunks to keep in cache
     *                              in case they need to be reloaded.
     * @param cameraPosition position in the world of the camera. Update this
     *                       variable often!
     */
    public ChunkPool(Vector2i poolDistance, int additionalCacheSize, IBooleanGenerator2D generator, Vector2f cameraPosition) {
        this.poolDistance = poolDistance;
        this.cacheSize = (poolDistance.x * 2 + 1) * (poolDistance.y * 2 + 1) + additionalCacheSize;
        this.cameraPosition = cameraPosition;
        this.chunkQueue = new ArrayList<>();
        this.generator = generator;

        // Cache size must be at least as large as the number of
    }

    /**
     * Make sure that all chunks within poolDistance of the camera are loaded.
     * Slowest on first execution as all chunks need to be loaded.
     */
    public void update() {
        // Get chunk of camera
        Vector2i camChunk = ChunkUtils.getChunkAtCoord(cameraPosition);

        // Calculate which chunks need to be checked
        List<Vector2i> checkChunks = ChunkUtils.getChunksInDistance(camChunk, poolDistance);

        // Remove chunks that are loaded, so I'm left with chunks that aren't loaded
        synchronized (chunkQueue) {
            for (ChunkView chunk : chunkQueue) {
                Vector2i p = chunk.chunkPos;
                if (p.x >= camChunk.x - poolDistance.x && p.x <= camChunk.x + poolDistance.x &&
                        p.y >= camChunk.y - poolDistance.y && p.y <= camChunk.y + poolDistance.y) {
                    checkChunks.remove(chunk.getChunkPos());
                }
            }
        }

        // Loop through remaining unaccounted-for chunks and add them
        for (Vector2i chunkPos : checkChunks) {
            ChunkView chunk = new ChunkView(chunkPos);
            chunk.generate(generator);
            chunk.updateVertices();
            push(chunk);
        }
    }

    /**
     * Get the chunk given by the coordinates. This will also move
     * the chunk to the top of the cache so it's priority is refreshed.
     *
     * @param chunkPos
     * @return
     */
    public ChunkView get(Vector2i chunkPos) {
        for (int i = 0; i < chunkQueue.size(); i++) {
            if (chunkQueue.get(i).getChunkPos().equals(chunkPos)) {
                ChunkView chunk = chunkQueue.get(i);
                chunkQueue.remove(i);
                chunkQueue.add(chunk);
                return chunk;
            }
        }
        return null;
    }

    /**
     * Add a chunk to the pool, and make sure that old chunks get
     * removed based on the cache size. Is thread safe.
     *
     * @param chunkView chunk to add.
     */
    private void push(ChunkView chunkView) {
        // Make sure the size doesn't go over the cacheSize
        synchronized (chunkQueue) {
            if (chunkQueue.size() >= cacheSize) {
                chunkQueue.remove(0);
            }
            chunkQueue.add(chunkView);
        }
    }

    public void setCameraPosition(Vector2f cameraPosition) {
        this.cameraPosition = cameraPosition;
    }

    /**
     * Update the chunk pool based on the camera position.
     * You should call setCameraPosition before this.
     * If the pool is being updated currently, this will do nothing.
     */
    @Override
    public void run() {
        update();
    }
}
