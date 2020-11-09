package com.spooky.engine.shader;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;

public class Shader {

    private int shaderID;

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
        int vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexID, vertexShader);
        GL20.glCompileShader(vertexID);

        // Check for errors
        int success =  GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS);
        if (success == GL20.GL_FALSE) {
            int len = GL20.glGetShaderi(vertexID, GL20.GL_INFO_LOG_LENGTH);
            System.out.println("Error: " + vertexFilepath + " failed to compile");
            System.out.println(GL20.glGetShaderInfoLog(vertexID, len));
            assert false;
        }

        // Compile fragment
        int fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentID, fragmentShader);
        GL20.glCompileShader(fragmentID);

        // Check for errors
        success =  GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS);
        if (success == GL20.GL_FALSE) {
            int len = GL20.glGetShaderi(fragmentID, GL20.GL_INFO_LOG_LENGTH);
            System.out.println("Error: " + fragmentFilepath + " failed to compile");
            System.out.println(GL20.glGetShaderInfoLog(fragmentID, len));
            assert false;
        }

        // Create shader program
        shaderID = GL20.glCreateProgram();
        GL20.glAttachShader(shaderID, vertexID);
        GL20.glAttachShader(shaderID, fragmentID);
        GL20.glLinkProgram(shaderID);

        // Check for linking errors
        success = GL20.glGetProgrami(shaderID, GL20.GL_LINK_STATUS);
        if (success == GL20.GL_FALSE) {
            int len = GL20.glGetProgrami(shaderID, GL20.GL_INFO_LOG_LENGTH);
            System.out.println("Error: linking of " + vertexFilepath + " and " + fragmentFilepath + " failed.");
            System.out.println(GL20.glGetProgramInfoLog(shaderID, len));
            assert false;
        }
    }

    public void use() {
        GL20.glUseProgram(shaderID);
    }

    public void detach() {
        GL20.glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat) {
        int varLocation = GL20.glGetUniformLocation(shaderID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer);
        GL20.glUniformMatrix4fv(varLocation, false, matBuffer);
    }

}
