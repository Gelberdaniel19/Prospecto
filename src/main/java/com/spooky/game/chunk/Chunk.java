package com.spooky.game.chunk;

import com.spooky.engine.Color;
import com.spooky.game.noise.IBooleanGenerator2D;
import org.joml.Vector2i;

/**
 * A chunk represented completely separately from
 * the rendering system.
 */
public class Chunk {
    // Width and height of each chunk
    public static final int CHUNK_SIZE = 128;

    // Position of the chunk is where the bottom left is times 128
    protected final Vector2i chunkPos;

    // Actual data held by chunk
    protected final Block[][] chunkBlocks = new Block[CHUNK_SIZE][CHUNK_SIZE];

    public Chunk(Vector2i chunkCoords) {
        this.chunkPos = chunkCoords;
    }

    public void generate(IBooleanGenerator2D generator) {
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                Vector2i worldCoords = new Vector2i(chunkPos.x * CHUNK_SIZE + i, chunkPos.y * CHUNK_SIZE + j);
                boolean isBlock = generator.getValue(worldCoords.x, worldCoords.y);
                setPixel(i, j, new Block(worldCoords, isBlock ? Color.WHITE : Color.TRANSPARENT));
                if (generator.getValue(worldCoords.x, worldCoords.y)) {
                    setPixel(i, j, new Block(worldCoords, Color.WHITE));
                } else {
                    setPixel(i, j, new Block(worldCoords, Color.TRANSPARENT));
                }
            }
        }
    }

    /**
     * Replaces the pixel at the coordinate with the pixel p.
     *
     * @param x coordinate in chunk.
     * @param y coordinate in chunk.
     * @param p new pixel.
     */
    public void setPixel(int x, int y, Block p) {
        chunkBlocks[x][y] = p;
    }

    /**
     * Sets the color of the pixel at given coordinates.
     *
     * @param x     coordinate in chunk.
     * @param y     coordinate in chunk.
     * @param color new color.
     */
    public void setPixelColor(int x, int y, Color color) {
        chunkBlocks[x][y].color = color;
    }

    /**
     * Returns a copy of the pixel at the coordinates.
     * Modifying this pixel will not change its actual value. To change
     * the pixel's data, use a setter function.
     *
     * @param x coordinate in chunk.
     * @param y coordinate in chunk.
     * @return copy of the pixel.
     */
    public Block getBlock(int x, int y) {
        return chunkBlocks[x][y].copy();
    }

    public Vector2i getChunkPos() {
        return chunkPos;
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "pos=(" + chunkPos.x + ", " + chunkPos.y + ")" +
                '}';
    }
}
