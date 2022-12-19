package com.example.engineandroid;


public interface Scene {
    //Main Functions
    public void update(double deltaTime, AdManager adManager);
    public void render(RenderAndroid render);
    public void handleInput(EventHandler.EventType type, AdManager adManager);
    public void onResume();

    public boolean inputReceived(Vector2D pos, Vector2D size);
    public void init();

    //Allows the loading of resources inside, only called automatically on the engine PrimaryScene
    public void loadResources(EngineApp engineAux);
    }
