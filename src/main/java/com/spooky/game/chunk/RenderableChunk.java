package com.spooky.game.chunk;

import com.spooky.engine.Camera;

public class RenderableChunk extends Chunk {

    private ChunkRenderer renderer;
    private Camera camera = null;

    public RenderableChunk(int x, int y) {
        super(x, y);
        renderer = new ChunkRenderer(this);
        renderer.start();
    }

    public RenderableChunk(int x, int y, Camera camera) {
        super(x, y);
        this.camera = camera;
        renderer = new ChunkRenderer(this);
        renderer.start();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void render() {
        try {
            renderer.render(camera);
        } catch (NullPointerException e) {
            e.printStackTrace();
            assert false : "Set the camera object in the renderable chunk!";
        }
    }

}
