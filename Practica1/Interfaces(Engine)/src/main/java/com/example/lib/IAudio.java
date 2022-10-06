package com.example.lib;

public interface IAudio{
    //Main function
    public ISound newSound();

    //Other functions
}

interface ISound{
    public void play();
    public void stop();
}
