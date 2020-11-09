package com.spooky.engine.util;

import org.lwjgl.glfw.GLFW;

/**
 * Class for checking the state of the keyboard.
 */
public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];

    private KeyListener(){

    }

    public static KeyListener get(){
        if(instance == null){
            instance = new KeyListener();
        }

        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action == GLFW.GLFW_PRESS){
            get().keyPressed[key] = true;
        } else if (action == GLFW.GLFW_RELEASE){
            get().keyPressed[key] = false;
        }
    }

    /**
     * Returns whether or not a given key is pressed.
     * @param keyCode the key code of the key EX. KeyEvent.VK_SPACE
     * @return true if the key is pressed.
     */
    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }

}
