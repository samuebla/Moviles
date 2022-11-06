package com.example.lib;


public interface Scene {
    //Main Functions
    public void update(double deltaTime);
    public void render();
    public void handleInput();
}
