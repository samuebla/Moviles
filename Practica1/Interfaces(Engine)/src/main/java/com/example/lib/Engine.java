package com.example.lib;

public interface Engine {
    //Main functions
    public IGraphics getGraphics();
    public IAudio getAudio();
    public IState getState();
    public Input getInput();
    public IEventHandler getEventMngr();

    //Other functions(customized)
    public void paintCell(int x1,int x2, int y1,int y2, int typeAux);
    public void drawText(String text, int x, int y, String color,IFont font);
//    public void addComponent(Component aux);
    public void setScene(Scene newScene);
}