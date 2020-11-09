package com.spooky.engine.graphics;

import com.spooky.engine.Transform;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pixel {
    private Transform pos;
    private Color color;
}
