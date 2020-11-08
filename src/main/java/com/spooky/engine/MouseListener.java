package com.spooky.engine;

import org.lwjgl.glfw.GLFW;

/**
 * Class for checking the state of the mouse.
 */
public class MouseListener {

    private static MouseListener instance = null;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[5];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;

        // If mouse just moved, if a button is pressed, then set dragging true
        get().isDragging =
                get().mouseButtonPressed[0] ||
                get().mouseButtonPressed[1] ||
                get().mouseButtonPressed[2] ||
                get().mouseButtonPressed[3] ||
                get().mouseButtonPressed[4];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        // If they pressed a button
        if (action == GLFW.GLFW_PRESS) {
            // Set the button to pressed if the button is in the array
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        }

        // Otherwise, the mouse button is not pressed and they are no longer dragging
        else {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void scrollCallback(long window, double xOff, double yOff) {
        get().scrollX = xOff;
        get().scrollY = yOff;
    }

    /**
     * Call this when the frame ends to reset variables for the next frame.
     */
    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }

    public static float getDx() {
        return (float)(get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean isMouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }

}
