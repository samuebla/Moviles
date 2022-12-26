package com.example.lib;

public interface ISceneMngr {

    public void popScene();
    public void pushScene(Scene scene);
    public void update(double deltaTime);
    public void render(IGraphics render);

    //Allows the loading of resources
    public void loadResources(Engine engine);

    public void handleInput(IEventHandler.EventType type, ISound sound, Input input, ISceneMngr sceneMngr);
    public Scene getScene();
}