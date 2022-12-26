package com.example.lib;


import java.awt.geom.Point2D;

public interface Scene {
    //Main Functions
    public void update(double deltaTime);
    public void render();
    public void handleInput();
    public void init();
    }
