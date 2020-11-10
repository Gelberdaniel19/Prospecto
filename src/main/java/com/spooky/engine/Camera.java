package com.spooky.engine;

import com.spooky.game.chunk.Block;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;

    private final Vector2f position;

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjectionToPixelsPerBlock(Block.PIXELS_PER_BLOCK);
    }

    public void adjustProjectionToPixelsPerBlock(int pixelsPerBlock) {
        projectionMatrix.identity();
        float windowUnitsWide = (float)Window.getWidth() / pixelsPerBlock;
        float windowUnitsTall = (float)Window.getHeight() / pixelsPerBlock;
        projectionMatrix.ortho(-windowUnitsWide / 2, windowUnitsWide / 2,
                -windowUnitsTall / 2, windowUnitsTall / 2,
                0.0f, 100.0f);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.identity();
        viewMatrix.lookAt(
                new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void move(Vector2f delta) {
        position.add(delta);
    }

    public Vector2f getPosition() {
        return position;
    }
}
