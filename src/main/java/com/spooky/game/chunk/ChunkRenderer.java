package com.spooky.game.chunk;

import com.spooky.engine.Shader;
import com.spooky.engine.Camera;

import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Holds a chunk view and draws it
 */
public class ChunkRenderer {

    private final ChunkView chunk;
    private final Shader shader;
    private int vaoID, vboID;

    public ChunkRenderer(ChunkView chunk) {
        this.chunk = chunk;
        this.shader = new Shader("vertex.glsl", "fragment.glsl");
    }

    public void start() {
        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, chunk.getVertices().length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = chunk.getIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        int POS_SIZE = 2;
        int POS_OFFSET = 0;
        int VERTEX_SIZE_BYTES = ChunkView.VERTEX_SIZE * Float.BYTES;
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        int COLOR_SIZE = 4;
        int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void render(Camera camera) {
        // If needs to update
        if (chunk.needsToUpdateView()) {
            // Re-buffer on updates (used to be every frame)
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, chunk.getVertices());
            chunk.setNeedsToUpdateView(false);
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

    public ChunkView getChunk() {
        return chunk;
    }

    @Override
    public String toString() {
        return chunk.toString();
    }
}
