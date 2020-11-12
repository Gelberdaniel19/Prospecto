package com.spooky.game.chunk;

import com.spooky.engine.Camera;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

/**
 * Load and unload RenderableChunks based on the render distance
 */
public class ChunkLoader {
    private final Camera camera;
    private final Vector2i renderDist;
    private final List<ChunkRenderer> chunks;
    private final ChunkPool chunkPool;

    public ChunkLoader(Vector2i renderDist, Camera camera, ChunkPool chunkPool) {
        this.renderDist = renderDist;
        this.camera = camera;
        this.chunkPool = chunkPool;
        chunks = new ArrayList<>();
    }

    /**
     * Make sure out of range chunks are unloaded and new in-range
     * chunks are loaded.
     * @param cameraWorldPosition world position of the camera.
     */
    public void update(Vector2f cameraWorldPosition) {
        // Get the chunk of the camera
        Vector2i camChunk = Chunk.getChunkAtCoord(cameraWorldPosition);

        // Calculate which chunks need to be checked
        List<Vector2i> checkChunks = new ArrayList<>();
        for (int i = 0; i < 2 * renderDist.x + 1; i++) {
            for (int j = 0; j < 2 * renderDist.y + 1; j++) {
                checkChunks.add(new Vector2i(camChunk.x - renderDist.x + i, camChunk.y - renderDist.y + j));
            }
        }

        // Remove coords which are loaded, leaving unloaded ones left
        for (ChunkRenderer renderer : chunks) {
            Vector2i p = renderer.getChunk().getChunkPos();
            if (p.x >= camChunk.x - renderDist.x && p.x <= camChunk.x + renderDist.x &&
                    p.y >= camChunk.y - renderDist.y && p.y <= camChunk.y + renderDist.y) {
                checkChunks.remove(renderer.getChunk().getChunkPos());
            }
        }

        // Loop through remaining unaccounted-for chunks and add them
        for (Vector2i chunkPos : checkChunks) {
            // Get chunk from pool, make sure it isn't null
            ChunkView chunk = chunkPool.get(chunkPos);
            if (chunk == null) {
                continue;
            }

            // Make and add renderer
            ChunkRenderer renderer = new ChunkRenderer(chunk);
            chunk.setNeedsToUpdateView(true);
            renderer.start();
            chunks.add(renderer);
        }

        // Remove all chunks that are outside the render distance
        for (int i = 0; i < chunks.size(); i++) {
            Vector2i p = chunks.get(i).getChunk().getChunkPos();
            if (!(p.x >= camChunk.x - renderDist.x && p.x <= camChunk.x + renderDist.x &&
                    p.y >= camChunk.y - renderDist.y && p.y <= camChunk.y + renderDist.y)) {
                chunks.remove(i);
                i--;
            }
        }
    }

    // Render all the chunks which are loader
    public void render(){
        for (ChunkRenderer renderer : chunks) {
            renderer.render(camera);
        }
    }

    /**
     * Get the number of chunks that will fit on the screen in both directions.
     * @param blocksPerChunk number of blocks in a chunk.
     * @param blockSize number of pixels per block.
     * @param windowWidth pixel width of window.
     * @param windowHeight pixel height of window.
     * @return x and y count of chunks that could fit on the screen
     */
    public static Vector2i getChunksOnScreen(int blocksPerChunk, int blockSize, int windowWidth, int windowHeight){
        int xChunks = (int)((float)windowWidth/blockSize/blocksPerChunk)+2;
        int yChunks = (int)((float)windowHeight/blockSize/blocksPerChunk)+2;
        return new Vector2i(xChunks, yChunks);
    }

    @Override
    public String toString() {
        return "ChunkLoader{" +
                chunks +
                "}";
    }
}