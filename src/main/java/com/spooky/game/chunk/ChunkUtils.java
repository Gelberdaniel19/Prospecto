package com.spooky.game.chunk;

import com.spooky.engine.Window;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of static utility functions for chunk
 * related operations.
 */
public class ChunkUtils {
    /**
     * Returns the chunk coordinates of the chunk at world position x, y
     *
     * @param worldCoord coordinates in the world to get the containing chunk of.
     * @return chunk coordinates of the chunk.
     */
    public static Vector2i getChunkAtCoord(Vector2i worldCoord) {
        Vector2i ret = new Vector2i(worldCoord.x / Chunk.CHUNK_SIZE, worldCoord.y / Chunk.CHUNK_SIZE);
        if (worldCoord.y < 0) ret.y--;
        if (worldCoord.x < 0) ret.x--;
        return ret;
    }

    /**
     * Returns the chunk coordinates of the chunk at world position x, y.
     *
     * @param worldCoord coordinates in the world to get the containing chunk of.
     * @return chunk coordinates of the chunk.
     */
    public static Vector2i getChunkAtCoord(Vector2f worldCoord) {
        Vector2i ret = new Vector2i((int) (worldCoord.x / Chunk.CHUNK_SIZE), (int) (worldCoord.y / Chunk.CHUNK_SIZE));
        if (worldCoord.y < 0) ret.y--;
        if (worldCoord.x < 0) ret.x--;
        return ret;
    }

    /**
     * Get a list of coordinates for chunks within the distance of an origin chunk.
     *
     * @param origin   center around which chunks are counted.
     * @param distance how many chunks in the x and y direction from the origin to count.
     * @return list of coordinates for chunks.
     */
    public static List<Vector2i> getChunksInDistance(Vector2i origin, Vector2i distance) {
        List<Vector2i> ret = new ArrayList<>();
        for (int i = 0; i < distance.x * 2 + 1; i++) {
            for (int j = 0; j < distance.y * 2 + 1; j++) {
                ret.add(new Vector2i(origin.x - distance.x + i, origin.y - distance.y + j));
            }
        }
        return ret;
    }

    public static Vector2i getChunksOnScreen() {
        int xChunks = (int) ((float) Window.getWidth() / Block.PIXELS_PER_BLOCK / Chunk.CHUNK_SIZE) + 2;
        int yChunks = (int) ((float) Window.getHeight() / Block.PIXELS_PER_BLOCK / Chunk.CHUNK_SIZE) + 2;
        return new Vector2i(xChunks, yChunks);
    }
}
