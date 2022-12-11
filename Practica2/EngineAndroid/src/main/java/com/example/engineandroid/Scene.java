package com.example.engineandroid;


public interface Scene {
    //Main Functions
    public void update(double deltaTime);
    public void render();
    public void handleInput(EventHandler.EventType type);
    public void onResume();
    public boolean inputReceived(Vector2D pos, Vector2D size);
    public void init();
    }
