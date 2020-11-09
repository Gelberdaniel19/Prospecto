package com.spooky.game.chunk;

import com.spooky.engine.Color;
import org.joml.Vector2i;

public class Block {
    public final static int PIXELS_PER_BLOCK = 4;

    public Vector2i pos;
    public Color color;

    public Block(Vector2i pos, Color color) {
        this.pos = pos;
        this.color = color;
    }

    public Block copy() {
        return new Block(new Vector2i(pos.x, pos.y), new Color(color.r, color.g, color.b));
    }
}
