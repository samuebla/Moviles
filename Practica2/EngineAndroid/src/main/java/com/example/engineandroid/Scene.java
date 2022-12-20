package com.example.engineandroid;


public interface Scene {
    //Main Functions
    void update(double deltaTime);
    void render(RenderAndroid render);
    void handleInput(EventHandler.EventType type, AdManager adManager, InputAndroid input, SceneMngrAndroid sceneMngr, AudioAndroid audio, RenderAndroid render);
    void onResume();

    void init();

    void onStop();

    //Allows the loading of resources inside, only called automatically on the engine PrimaryScene
    void loadResources(EngineApp engineAux);
    }
