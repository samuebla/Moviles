package com.example.lib;


public interface Engine {
    //Main functions
    public IGraphics getGraphics();
    public IAudio getAudio();
    public IState getState();

    //Other functions(customized)

}