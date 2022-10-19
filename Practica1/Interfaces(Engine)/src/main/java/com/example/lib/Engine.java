package com.example.lib;

import java.awt.Component;

public interface Engine {
    //Main functions
    public IGraphics getGraphics();
    public IAudio getAudio();
    public IState getState();

    //Other functions(customized)
    public void paintCell(int x1,int x2, int y1,int y2, int typeAux);
//    public void addComponent(Component aux);
    public void setScene(Scene newScene);

    public void setWrongCells(String text);
    public void setRemainingCells(String text);
}