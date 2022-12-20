package com.example.engineandroid;


public interface Scene {
    //Funciones principales por orden de realizacion
    void update(double deltaTime);
    void handleInput(EventHandler.EventType type, AdManager adManager, InputAndroid input, SceneMngrAndroid sceneMngr, AudioAndroid audio, RenderAndroid render);
    void render(RenderAndroid render);

    //No se llama en el engine, la escena puede definirlo paar que quede mas clara la inicialización y asi dividir el codigo.
    void init();

    //Llamado al interrumpirse la aplicación
    void onStop();

    //Allows the loading of resources inside, only called automatically on the engine PrimaryScene
    void loadResources(EngineApp engineAux);
    }
