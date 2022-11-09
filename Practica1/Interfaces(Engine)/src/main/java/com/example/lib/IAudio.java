package com.example.lib;

public interface IAudio{
    //Main function
    public ISound newSound(String audioName,String path);
    public void loadMusic(String audioName, String path);
    public void playSound(String audioName, int type);
    public void setLoop(String audioName);

    //Other functions
}

