package com.example.lib;

public interface Engine {
    //Main functions
    IGraphics getGraphics();
    IAudio getAudio();
    Input getInput();
    IEventHandler getEventMngr();
    ISceneMngr getSceneMngr();
    void setResourceScene(Scene scene);
}