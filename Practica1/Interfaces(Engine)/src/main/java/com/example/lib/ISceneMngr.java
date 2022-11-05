package com.example.lib;

public interface ISceneMngr {

    public void popScene();
    public void pushScene(Scene scene);
    public void update();
    public void render();
    public void handleInput();
}