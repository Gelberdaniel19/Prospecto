package com.spooky.game.chunk;

import com.spooky.engine.Color;
import org.joml.Vector2i;

/**
 * A chunk which also keeps its vertex array and has the
 * ability to update it's vertex array.
 */
public class ChunkView extends Chunk {

    public final static int VERTEX_SIZE = 6;
    public final static int MAX_BATCH_SIZE = Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE;

    private final float[] vertices;
    private final int[] indices;

    // Tells classes whether or not it needs to be updated
    protected boolean needsToUpdateModel = true;
    protected boolean needsToUpdateView = true;

    public ChunkView(Vector2i chunkCoords) {
        super(chunkCoords);
        this.vertices = new float[MAX_BATCH_SIZE * 4 * VERTEX_SIZE];
        this.indices = generateIndices();
    }

    public void updateVertices() {
        synchronized (vertices) {
            // For all blocks in the chunk
            for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
                for (int j = 0; j < Chunk.CHUNK_SIZE; j++) {
                    // Find offset within vertex array
                    int index = i * Chunk.CHUNK_SIZE + j;
                    int offset = index * 4 * VERTEX_SIZE;

                    // Add vertices with the appropriate properties
                    float xAdd = 1.0f;
                    float yAdd = 1.0f;
                    for (int k = 0; k < 4; k++) {
                        if (k == 1) yAdd = 0.0f;
                        else if (k == 2) xAdd = 0.0f;
                        else if (k == 3) yAdd = 1.0f;

                        // Set position and color into vertex array
                        Block block = chunkBlocks[i][j];
                        vertices[offset + 0] = block.pos.x + xAdd;
                        vertices[offset + 1] = block.pos.y + yAdd;
                        vertices[offset + 2] = block.color.fGetR();
                        vertices[offset + 3] = block.color.fGetG();
                        vertices[offset + 4] = block.color.fGetB();
                        vertices[offset + 5] = block.color.fGetA();

                        offset += VERTEX_SIZE;
                    }
                }
            }

            needsToUpdateModel = false;
            needsToUpdateView = true;
        }
    }

    public boolean needsToUpdateModel() {
        return needsToUpdateModel;
    }

    public void setNeedsToUpdateModel(boolean needsToUpdateModel) {
        this.needsToUpdateModel = needsToUpdateModel;
    }

    public boolean needsToUpdateView() {
        return needsToUpdateView;
    }

    public void setNeedsToUpdateView(boolean needsToUpdateView) {
        this.needsToUpdateView = needsToUpdateView;
    }

    public float[] getVertices() {
        synchronized (vertices) {
            return vertices;
        }
    }

    public int[] getIndices() {
        return indices;
    }

    @Override
    public void setPixel(int x, int y, Block p) {
        super.setPixel(x, y, p);
        needsToUpdateModel = true;
    }

    @Override
    public void setPixelColor(int x, int y, Color color) {
        super.setPixelColor(x, y, color);
        needsToUpdateModel = true;
    }


    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * MAX_BATCH_SIZE];
        for (int i = 0; i < MAX_BATCH_SIZE; i++) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex + 0] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

}
