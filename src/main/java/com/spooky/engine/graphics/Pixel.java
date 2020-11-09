package com.spooky.engine.graphics;

import com.spooky.engine.Transform;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pixel {
    public static final int PIXEL_SIZE = 4;

    private Transform pos;
    private Color color;
}
