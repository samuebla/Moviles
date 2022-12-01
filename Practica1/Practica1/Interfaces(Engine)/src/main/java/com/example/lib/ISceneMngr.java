package com.example.lib;

public interface ISceneMngr {

    public void popScene();
    public void pushScene(Scene scene);
    public void update(double deltaTime);
    public void render();
    public void handleInput();
    public Scene getScene();
}