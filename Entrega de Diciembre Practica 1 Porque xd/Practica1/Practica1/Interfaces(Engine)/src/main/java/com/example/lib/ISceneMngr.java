package com.example.lib;

public interface ISceneMngr {

    void popScene();

    void pushScene(Scene scene);

    void update(double deltaTime);

    void render(IGraphics render);

    void handleInput(IEventHandler.EventType type, IAudio audio, Input input);

    Scene getScene();
}