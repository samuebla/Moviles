package com.example.lib;

public interface IAudio{
    //Main function
    public ISound newSound(String path);
    public void playSound(String audioName);

    //Other functions
}

