package com.example.engineandroid;


public interface Scene {
    //Main Functions
    void update(double deltaTime, AdManager adManager);
    void render(RenderAndroid render);
    void handleInput(EventHandler.EventType type, AdManager adManager);
    void onResume();

    boolean inputReceived(Vector2D pos, Vector2D size);
    void init();

    //Allows the loading of resources inside, only called automatically on the engine PrimaryScene
    void loadResources(EngineApp engineAux);
    }
