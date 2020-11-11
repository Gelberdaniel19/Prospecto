package com.spooky.engine;

public class Color {
    public short r;
    public short g;
    public short b;
    public short a;

    public float fGetR() {
        return (float)(r) / 255;
    }

    public float fGetG() {
        return (float)(g) / 255;
    }

    public float fGetB() {
        return (float)(b) / 255;
    }

    public float fGetA() {
        return (float)(a) / 255;
    }

    public Color(int r, int g, int b) {
        this.r = (short)r;
        this.g = (short)g;
        this.b = (short)b;
        this.a = (short)255;
    }

    public Color(int r, int g, int b, int a) {
        this.r = (short)r;
        this.g = (short)g;
        this.b = (short)b;
        this.a = (short)a;
    }


    public static Color fromFloats(float r, float g, float b) {
        return new Color((short)(r * 255), (short)(g * 255), (short)(b * 255));
    }

    public static Color random() {
        short r = (short)(Math.random() * 256);
        short g = (short)(Math.random() * 256);
        short b = (short)(Math.random() * 256);
        return new Color(r, g, b);
    }

    public static Color grey(short val) {
        return new Color(val, val, val);
    }

    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color RED = new Color(255, 0, 0);
    public static final Color GREEN = new Color(255, 0, 0);
    public static final Color BLUE = new Color(255, 0, 0);
    public static final Color ORANGE = new Color(255, 150, 0);
    public static final Color YELLOW = new Color(255, 255, 0);
    public static final Color AQUA = new Color(0, 255, 255);
    public static final Color PURPLE = new Color(150, 0, 255);
    public static final Color MAGENTA = new Color(255, 0, 255);
    public static final Color TRANSPARENT = new Color(255, 255, 0, 0);

}
