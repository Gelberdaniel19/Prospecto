package com.spooky.engine;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private static final FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
    private final int shaderID;

    public Shader(String vertexFilepath, String fragmentFilepath) {
        // Get source code of vertex and fragment
        String vertexShader = "", fragmentShader = "";
        try {
            ClassLoader classLoader = Shader.class.getClassLoader();
            File fileV = new File(classLoader.getResource("shaders/" + vertexFilepath).getFile());
            vertexShader = new String(Files.readAllBytes(fileV.toPath()));
            File fileF = new File(classLoader.getResource("shaders/" + fragmentFilepath).getFile());
            fragmentShader = new String(Files.readAllBytes(fileF.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }

        // Compile vertex shader
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexShader);
        glCompileShader(vertexID);

        // Check for errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: " + vertexFilepath + " failed to compile");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false;
        }

        // Compile fragment
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentShader);
        glCompileShader(fragmentID);

        // Check for errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: " + fragmentFilepath + " failed to compile");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false;
        }

        // Create shader program
        shaderID = glCreateProgram();
        glAttachShader(shaderID, vertexID);
        glAttachShader(shaderID, fragmentID);
        glLinkProgram(shaderID);

        // Check for linking errors
        success = glGetProgrami(shaderID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: linking of " + vertexFilepath + " and " + fragmentFilepath + " failed.");
            System.out.println(glGetProgramInfoLog(shaderID, len));
            assert false;
        }
    }

    public void use() {
        glUseProgram(shaderID);
    }

    public void detach() {
        glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat) {
        int varLocation = glGetUniformLocation(shaderID, varName);
        mat.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

}
