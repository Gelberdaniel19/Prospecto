package com.spooky.engine.graphics;

import com.spooky.engine.Shader;
import com.spooky.engine.flow.Window;
import com.spooky.engine.util.Camera;
import org.joml.Vector4f;

import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL20.*;

public class BatchPixelRenderer {
    private final float PIXEL_SIZE = 100.0f;

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 3;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 5;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private Pixel[] pixels;
    private int numPixels;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public BatchPixelRenderer(int maxBatchSize) {
        this.shader = new Shader("vertex.glsl", "fragment.glsl");
        this.maxBatchSize = maxBatchSize;
        this.pixels = new Pixel[maxBatchSize];

        // 4 vertices per pixel
        this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numPixels = 0;
        this.hasRoom = true;
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
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void render(Camera camera) {
        // For now, we will re-buffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4f("uView", camera.getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numPixels * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    public void addPixel(Pixel pixel) {
        // Get index and add renderObject
        int index = this.numPixels;
        this.pixels[index] = pixel;
        this.numPixels++;

        // Add properties to local vertices array
        loadVertexProperties(index);

        if (numPixels >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }

    private void loadVertexProperties(int index) {
        Pixel pixel = this.pixels[index];
        Color color = pixel.getColor();

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        // Add vertices with the appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i=0; i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }

            // Load position
            vertices[offset] = pixel.getPos().getX() + (xAdd * PIXEL_SIZE);
            vertices[offset + 1] = pixel.getPos().getY() + (yAdd * PIXEL_SIZE);

            // Load color
            vertices[offset + 2] = color.fGetR();
            vertices[offset + 3] = color.fGetG();
            vertices[offset + 4] = color.fGetB();

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }

}
