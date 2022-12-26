package com.example.lib;

public interface ISceneMngr {

    public void popScene();

    public void pushScene(Scene scene);

    public void update(double deltaTime);

    public void render(IGraphics render);

    public void handleInput(IEventHandler.EventType type, IAudio audio, Input input, ISceneMngr sceneMngr);

    public Scene getScene();
}