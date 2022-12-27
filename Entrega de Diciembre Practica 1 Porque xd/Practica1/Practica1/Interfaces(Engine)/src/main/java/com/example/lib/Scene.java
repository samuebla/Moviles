package com.example.lib;


public interface Scene {
    //Main Functions
    void update(double deltaTime);
    void render(IGraphics render);
    //Allows the loading of resources
    void loadResources(Engine engine);
    void handleInput(IEventHandler.EventType type, IAudio audio, Input input, ISceneMngr sceneMngr);
    void init();
}
