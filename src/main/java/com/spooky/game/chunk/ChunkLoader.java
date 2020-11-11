package com.spooky.game.chunk;

import com.spooky.engine.Camera;
import com.spooky.game.noise.IBooleanGenerator2D;
import com.spooky.game.noise.IGreyscaleGenerator2D;
import org.joml.Vector2f;
import org.joml.Vector2i;

/**
 * Load and unload chunks based on a camera position.
 */
public class ChunkLoader {
    private final IBooleanGenerator2D generator2D;
    private final Camera camera;
    private final int renderDistanceY;
    private final int renderDistanceX;
    private RenderableChunk[][] chunks = null;

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

    public ChunkLoader(int renderDistanceX, int renderDistanceY, IBooleanGenerator2D generator2D, Camera camera) {
        this.renderDistanceX = renderDistanceX;
        this.renderDistanceY = renderDistanceY;
        this.generator2D = generator2D;
        this.camera = camera;
    }

    /**
     * Make sure out of range chunks are unloaded and new in-range
     * chunks are loaded.
     * @param cameraWorldPosition world position of the camera.
     */
    public void update(Vector2f cameraWorldPosition) {
        // Generate chunks if they don't yet exist
        if (chunks == null) {
            generateChunks(cameraWorldPosition);
            return;
        }

        // Get the chunk which contains the camera and the current center of the chunk loader
        Vector2i cameraChunk = Chunk.getChunkAtCoord((int) cameraWorldPosition.x, (int) cameraWorldPosition.y);
        Vector2i centerChunk = new Vector2i(chunks[renderDistanceX][renderDistanceY].getX(), chunks[renderDistanceX][renderDistanceY].getY());

        // Return if the chunk doesn't need to be updated
        if (cameraChunk.equals(centerChunk)) {
            return;
        }

        // Teleportation case: regenerate all chunks
        if (cameraChunk.distance(centerChunk) >= 2) {
            generateChunks(cameraWorldPosition);
            return;
        }

        // If moved to right, shift all columns to left
        if (cameraChunk.x > centerChunk.x) {
            for (int i = 0; i < chunks.length-1; i++) {
                for (int j = 0; j < chunks[i].length; j++) {
                    chunks[i][j] = chunks[i+1][j];
                }
            }
            // Update rightmost col
            for (int i = 0; i < chunks[0].length; i++) {
                chunks[chunks.length-1][i] = new RenderableChunk(cameraChunk.x + renderDistanceX, centerChunk.y - renderDistanceY + i, camera);
                chunks[chunks.length-1][i].generate(generator2D);
            }
        }

        // If moved to left, shift all columns right
        else if (cameraChunk.x < centerChunk.x) {
            for (int i = chunks.length-1; i > 0; i--) {
                for (int j = 0; j < chunks[i].length; j++) {
                    chunks[i][j] = chunks[i-1][j];
                }
            }
            // Update leftmost column
            for (int i = 0; i < chunks[0].length; i++) {
                chunks[0][i] = new RenderableChunk(cameraChunk.x - renderDistanceX, centerChunk.y - renderDistanceY + i, camera);
                chunks[0][i].generate(generator2D);
            }
        }

        // If moved up, shift all rows down
        if (cameraChunk.y > centerChunk.y) {
            for (int i = 0; i < chunks.length; i++) {
                for (int j = 0; j < chunks[i].length - 1; j++) {
                    chunks[i][j] = chunks[i][j+1];
                }
            }
            // Update topmost row
            for (int i = 0; i < chunks.length; i++) {
                chunks[i][chunks[i].length - 1] = new RenderableChunk(cameraChunk.x - renderDistanceX + i, cameraChunk.y + renderDistanceY, camera);
                chunks[i][chunks[i].length - 1].generate(generator2D);
            }
        }

        // If moved down, shift all rows up
        if (cameraChunk.y < centerChunk.y) {
            for (int i = 0; i < chunks.length; i++) {
                for (int j = chunks[i].length - 1; j > 0; j--) {
                    chunks[i][j] = chunks[i][j-1];
                }
            }
            // Update bottommost row
            for (int i = 0; i < chunks.length; i++) {
                chunks[i][0] = new RenderableChunk(cameraChunk.x - renderDistanceX + i, cameraChunk.y - renderDistanceY, camera);
                chunks[i][0].generate(generator2D);
            }
        }
    }

    private void generateChunks(Vector2f cameraWorldPosition) {
        Vector2i cameraChunk = Chunk.getChunkAtCoord((int) cameraWorldPosition.x, (int) cameraWorldPosition.y);
        chunks = new RenderableChunk[1 + 2 * renderDistanceX][1 + 2 * renderDistanceY];
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks[i].length; j++) {
                int chunkPositionX = cameraChunk.x - renderDistanceX + i;
                int chunkPositionY = cameraChunk.y - renderDistanceY + j;
                chunks[i][j] = new RenderableChunk(chunkPositionX, chunkPositionY);
                chunks[i][j].setCamera(camera);
                chunks[i][j].generate(generator2D);
            }
        }
    }

    // Render all the chunks which are loader
    public void render(){
        for (int i = 0; i < chunks.length; i++) {
            chunks[i][0].render();
            for (int j = 0; j < chunks[i].length; j++) {
                chunks[i][j].render();
            }
        }
    }

    @Override
    public String toString() {
        String output = "Loaded Chunks: \n";
        for (int i = 0; i < chunks.length; i++) {
            output += "\t";
            for (int j = 0; j < chunks[i].length; j++) {
                output += "(" + chunks[i][j].getX() + ", " + chunks[i][j].getY() + ")\t";
            }
            output += "\n";
        }
        return output;
    }
}