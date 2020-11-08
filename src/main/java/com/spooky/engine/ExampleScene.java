package com.spooky.engine;

/**
 * Example scene class
 */
public class ExampleScene extends Scene {

    @Override
    public void update(float deltaTime) {
        System.out.println(1/deltaTime);
    }

}
