package com.spooky.engine;

/**
 * Abstract scene class which contains functions that a scene would use.
 */
public abstract class Scene {

    protected Camera camera;

    /**
     * Called once per frame.
     *
     * @param deltaTime time elapsed in seconds since last call.
     */
    public abstract void update(float deltaTime);

    /**
     * Call this after construction.
     */
    public void init() {

    }

    public Camera getCamera() {
        return camera;
    }

}
