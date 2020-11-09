package com.spooky.engine.graphics;

public class Color {
    public int r, g, b;

    float fGetR() {
        return (float)(r) / 255;
    }

    float fGetG() {
        return (float)(g) / 255;
    }

    float fGetB() {
        return (float)(b) / 255;
    }

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Color fromFloats(float r, float g, float b) {
        return new Color((int)(r * 255), (int)(g * 255), (int)(b * 255));
    }

    public static Color random() {
        int r = (int)(Math.random() * 256);
        int g = (int)(Math.random() * 256);
        int b = (int)(Math.random() * 256);
        return new Color(r, g, b);
    }

    public static Color grey(int val) {
        return new Color(val, val, val);
    }

    public static final Color RED = new Color(255, 0, 0);
    public static final Color GREEN = new Color(255, 0, 0);
    public static final Color BLUE = new Color(255, 0, 0);
    public static final Color ORANGE = new Color(255, 150, 0);
    public static final Color YELLOW = new Color(255, 255, 0);
    public static final Color AQUA = new Color(0, 255, 255);
    public static final Color PURPLE = new Color(150, 0, 255);
    public static final Color MAGENTA = new Color(255, 0, 255);

}
