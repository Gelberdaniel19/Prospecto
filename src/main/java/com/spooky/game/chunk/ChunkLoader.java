package com.spooky.game.chunk;

import com.spooky.engine.Camera;
import com.spooky.game.noise.IGreyscaleGenerator2D;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class ChunkLoader {
    private final IGreyscaleGenerator2D generator2D;
    private final int renderDistanceY;
    private final int renderDistanceX;
    private RenderableChunk[][] chunks = null;
    private final Camera camera;

    // Get the max number of chunks that will fit on the screen at any given time
    public static Vector2i getChunksOnScreen(int blocksPerChunk, int blockSize, int windowWidth, int windowHeight){
        int xChunks = (int)Math.ceil(((float)windowWidth/(float)blockSize)/(float)blocksPerChunk)+1;
        int yChunks = (int)Math.ceil(((float)windowHeight/(float)blockSize)/(float)blocksPerChunk)+1;
        return new Vector2i(xChunks, yChunks);
    }

    // Create a chunk loader with some x and y render distances and a default generator.
    public ChunkLoader(int renderDistanceX, int renderDistanceY, IGreyscaleGenerator2D generator2D, Camera camera) {
        this.renderDistanceX = renderDistanceX;
        this.renderDistanceY = renderDistanceY;
        this.generator2D = generator2D;
        this.camera = camera;
    }


// Load a cube of chunks around the player. there should be (1 + 2*renderDistanceX) * (1 + 2*renderDistanceY) chunks loaded at any time.
// This should also unload any chunks outside the render distance
        //loader.update({float}camera.position.x, {float}camera.position.y);
    public void update(Vector2f cameraWorldPosition){
        if(chunks == null){
            generateChunks(cameraWorldPosition);
            return;
        }
        Vector2i cameraChunk = Chunk.getChunkAtCoord((int) cameraWorldPosition.x, (int) cameraWorldPosition.y);
        Vector2i centerChunk = new Vector2i(chunks[renderDistanceX][renderDistanceY].getX(), chunks[renderDistanceX][renderDistanceY].getY());
        if(!cameraChunk.equals(centerChunk)){
            // teleport
            if(cameraChunk.distance(centerChunk) >= 2){
                generateChunks(cameraWorldPosition);
                return;
            }
            // otherwise
            if(cameraChunk.x != centerChunk.x){
                if(cameraChunk.x > centerChunk.x){
                    // shift left and add right most column
                    for (int i = 0; i < chunks.length; i++) {
                        for (int j = 0; j < chunks[i].length-1; j++) {
                            chunks[i][j] = chunks[i][j+1];
                        }
                    }
                    for (int i = 0; i < chunks.length; i++) {
                        chunks[i][chunks[i].length-1] = new RenderableChunk(cameraChunk.x+renderDistanceX, cameraChunk.y-renderDistanceY+i, camera);
                        chunks[i][chunks[i].length-1].generate(generator2D);
                    }
                }
                else{
                    for (int i = 0; i < chunks.length; i++) {
                        for (int j = chunks[i].length-1; j > 0; j--) {
                            chunks[i][j] = chunks[i][j-1];
                        }
                    }
                    for (int i = 0; i < chunks.length; i++) {
                        chunks[i][0] = new RenderableChunk(cameraChunk.x-renderDistanceX, cameraChunk.y-renderDistanceY+i, camera);
                        chunks[i][0].generate(generator2D);
                    }
                }
            }
            if(cameraChunk.y != centerChunk.y){
                if(cameraChunk.y > centerChunk.y){

                }
                else{

                }

            }
        }

    }

    private void generateChunks(Vector2f cameraWorldPosition) {
        Vector2i cameraChunk = Chunk.getChunkAtCoord((int) cameraWorldPosition.x, (int) cameraWorldPosition.y);
        chunks = new RenderableChunk[1 + 2*renderDistanceX][1 + 2*renderDistanceY];
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks[i].length; j++) {
                int chunkPositionX = cameraChunk.x - renderDistanceX + i;
                int chunkPositionY = cameraChunk.y - renderDistanceY + j;
                chunks[i][j] = new RenderableChunk(chunkPositionX, chunkPositionY);
                chunks[i][j].setCamera(camera);
                chunks[i][j].generate(generator2D);
            }
        }
    }

    // Render all the chunks which are loader
    public void render(){
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks[i].length; j++) {
                chunks[i][j].render();
            }
        }
    }

// Example: if the render distance x is 9 and the render distance y is 3:
// These are the chunks which should be loaded, where the camera is positioned at the O
//
//     xxxxxxxxx|xxxxxxxxx
//     xxxxxxxxx|xxxxxxxxx
//     xxxxxxxxx|xxxxxxxxx
//     ---------O---------
//     xxxxxxxxx|xxxxxxxxx
//     xxxxxxxxx|xxxxxxxxx
//     xxxxxxxxx|xxxxxxxxx
}
