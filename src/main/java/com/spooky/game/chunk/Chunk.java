package com.spooky.game.chunk;

import com.spooky.engine.Color;
import com.spooky.game.noise.IGreyscaleGenerator2D;
import org.joml.Vector2i;

public class Chunk {
    // Width and height of each chunk
    public static final int CHUNK_SIZE = 16;

    // Position of the chunk is where the bottom left is times 128
    private int x;
    private int y;

    // Actual data held by chunk
    private Block[][] chunkBlocks = new Block[CHUNK_SIZE][CHUNK_SIZE];

    private boolean updatedSinceDraw;

    public Chunk(int x, int y) {
        this.x = x;
        this.y = y;
        updatedSinceDraw = true;
    }

    public void generate(IGreyscaleGenerator2D generator) {
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                int xCoord = x * CHUNK_SIZE + i;
                int yCoord = y * CHUNK_SIZE + j;
                float grey = generator.getValue(xCoord, yCoord) * 128 + 128;
                setPixel(i, j, new Block(new Vector2i(xCoord, yCoord), Color.grey((int)grey)));
            }
        }
    }

    /**
     * Replaces the pixel at the coordinate with the pixel p.
     * @param x coordinate in chunk.
     * @param y coordinate in chunk.
     * @param p new pixel.
     */
    public void setPixel(int x, int y, Block p) {
        chunkBlocks[x][y] = p;
        updatedSinceDraw = true;
    }

    /**
     * Sets the color of the pixel at given coordinates.
     * @param x coordinate in chunk.
     * @param y coordinate in chunk.
     * @param color new color.
     */
    public void setPixelColor(int x, int y, Color color) {
        chunkBlocks[x][y].color = color;
        updatedSinceDraw = true;
    }

    /**
     * Returns a copy of the pixel at the coordinates.
     * Modifying this pixel will not change its actual value. To change
     * the pixel's data, use a setter function.
     * @param x coordinate in chunk.
     * @param y coordinate in chunk.
     * @return copy of the pixel.
     */
    public Block getBlock(int x, int y) {
        return chunkBlocks[x][y].copy();
    }

    public void setUpdatedSinceDraw(boolean val) {
        updatedSinceDraw = val;
    }

    public boolean isUpdatedSinceDraw() {
        return updatedSinceDraw;
    }

    /**
     * Returns the chunk coordinates of the chunk at world position x, y
     * @param x coordinate in world.
     * @param y coordinate in world.
     * @return chunk coordinates of the chunk.
     */
    public static Vector2i getChunkAtCoord(int x, int y) {
        Vector2i ret = new Vector2i(x / CHUNK_SIZE, y / CHUNK_SIZE);
        if (y < 0) ret.y--;
        if (x < 0) ret.x--;
        return ret;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
