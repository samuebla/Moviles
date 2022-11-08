package com.example.lib;

public interface Engine {
    //Main functions
    public IGraphics getGraphics();
    public IAudio getAudio();
    public IState getState();
    public Input getInput();
    public IEventHandler getEventMngr();

    //Other functions(customized)
    public void paintCell(int x,int y, int w,int h, int typeAux);
    public void drawText(String text, int x, int y, String color,IFont font);
    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, IImage image);
//    public void addComponent(Component aux);
    public void setScene(Scene newScene);
    public void setSceneMngr(ISceneMngr sceneMngr);
    public void popScene();
}