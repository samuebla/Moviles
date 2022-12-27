package com.example.lib;

public interface IAudio{
    //Main function
    ISound newSound(String audioName,String path);
    void loadMusic(String audioName, String path);
    void playSound(String audioName, int type);
    void setLoop(String audioName);
}

