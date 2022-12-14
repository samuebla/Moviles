package com.example.lib;


import java.awt.geom.Point2D;

public interface Scene {
    //Main Functions
    public void update(double deltaTime);
    public void render();
    public void handleInput();
    public boolean inputReceived(Vector2D pos, Vector2D size);
    public void init();
    }
