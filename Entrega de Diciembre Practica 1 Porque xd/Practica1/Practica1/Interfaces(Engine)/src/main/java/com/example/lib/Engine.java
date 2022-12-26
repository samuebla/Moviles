package com.example.lib;

public interface Engine {
    //Main functions
    public IGraphics getGraphics();
    public IAudio getAudio();
    public Input getInput();
    public IEventHandler getEventMngr();
    public ISceneMngr getSceneMngr();
    public void setResourceScene(Scene scene);
}