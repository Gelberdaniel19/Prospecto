package com.spooky.engine.graphics;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector4f;

@Data
@AllArgsConstructor
public class Color {
    int r, g, b;

    float fGetR() {
        return (float)(r) / 255;
    }

    float fGetG() {
        return (float)(g) / 255;
    }

    float fGetB() {
        return (float)(b) / 255;
    }

    Vector4f getColor4f() {
        return new Vector4f(fGetR(), fGetG(), fGetB(), 1);
    }
}
