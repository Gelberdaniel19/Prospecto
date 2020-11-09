package com.spooky.engine;

import com.spooky.engine.shader.Shader;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexArrayObject;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Example scene class
 */
public class ExampleScene extends Scene {

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
            150f, 50f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            50f,  150f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            150f,  150f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            50f, 50f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
    };

    private int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };

    private int vaoID, vboID, eboID;

    private Shader shader;

    @Override
    public void update(float deltaTime) {
        shader.use();
        shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4f("uView", camera.getViewMatrix());

        // Bind the VAO that we're using
        ARBVertexArrayObject.glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL20.glDrawElements(GL20.GL_TRIANGLES, elementArray.length, GL20.GL_UNSIGNED_INT, 0);

        // Unbind everything
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        ARBVertexArrayObject.glBindVertexArray(0);

        shader.detach();
    }

    @Override
    public void init() {
        // Set up camera
        camera = new Camera(new Vector2f());

        // Set up shader
        shader = new Shader("vertex.glsl", "fragment.glsl");

        vaoID = ARBVertexArrayObject.glGenVertexArrays();
        ARBVertexArrayObject.glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = GL20.glGenBuffers();
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vboID);
        GL20.glBufferData(GL20.GL_ARRAY_BUFFER, vertexBuffer, GL20.GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = GL20.glGenBuffers();
        GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, eboID);
        GL20.glBufferData(GL20.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL20.GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        GL20.glVertexAttribPointer(0, positionsSize, GL20.GL_FLOAT, false, vertexSizeBytes, 0);
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(1, colorSize, GL20.GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        GL20.glEnableVertexAttribArray(1);
    }
}
