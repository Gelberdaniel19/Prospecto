package com.spooky.game.chunk;

import com.spooky.engine.Shader;
import com.spooky.engine.Camera;

import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL20.*;

public class ChunkRenderer {

    private final int VERTEX_SIZE = 5;

    private final Chunk chunk;
    private final float[] vertices;

    private int vaoID, vboID;
    private final int MAX_BATCH_SIZE = Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE;
    private final Shader shader;

    public ChunkRenderer(Chunk chunk) {
        this.chunk = chunk;
        this.shader = new Shader("vertex.glsl", "fragment.glsl");
        this.vertices = new float[MAX_BATCH_SIZE * 4 * VERTEX_SIZE];
    }

    public void start() {
        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        int POS_SIZE = 2;
        int POS_OFFSET = 0;
        int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        int COLOR_SIZE = 3;
        int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void render(Camera camera) {
        // If needs to update
        if (chunk.isUpdatedSinceDraw()) {
            // Load pixels from chunk
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
                        Block block = chunk.getBlock(i, j);
                        vertices[offset + 0] = block.pos.x + xAdd;
                        vertices[offset + 1] = block.pos.y + yAdd;
                        vertices[offset + 2] = block.color.fGetR();
                        vertices[offset + 3] = block.color.fGetG();
                        vertices[offset + 4] = block.color.fGetB();

                        offset += VERTEX_SIZE;
                    }
                }
            }

            chunk.setUpdatedSinceDraw(false);

            // Re-buffer on updates (used to be every frame)
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4f("uView", camera.getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
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
