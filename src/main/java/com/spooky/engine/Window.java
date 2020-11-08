package com.spooky.engine;

import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {

    private int width;
    private int height;
    private String title;
    private long glfwWindow;

    private static Window instance = null;

    private Window() {
        this.width = 1080;
        this.height = 720;
        this.title = "Prospecto";
    }

    public static Window get() {
        if (instance == null) {
            instance = new Window();
        }
        return instance;
    }

    public void run() {
        System.out.println("Hello! " + Version.getVersion());
        init();
        loop();

        // Free memory
        Callbacks.glfwFreeCallbacks(glfwWindow);
        GLFW.glfwDestroyWindow(glfwWindow);

        // Terminate GLFW
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Set up error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_FALSE);

        // Create the window
        glfwWindow = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (glfwWindow == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to create GLFW window");
        }

        // Register callbacks
        GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        GLFW.glfwSetScrollCallback(glfwWindow, MouseListener::scrollCallback);

        // Make the OpenGL context current this window
        GLFW.glfwMakeContextCurrent(glfwWindow);

        // Enable VSync
        GLFW.glfwSwapInterval(1);

        // Make the window visible
        GLFW.glfwShowWindow(glfwWindow);

        // IMPORTANT! Allows us to use the bindings
        GL.createCapabilities();
    }

    private void loop() {
        while (!GLFW.glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            GLFW.glfwPollEvents();

            // Clear screen
            GL11.glClearColor(1, 1, 1, 1);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            // Swap buffers
            GLFW.glfwSwapBuffers(glfwWindow);
        }
    }
}
