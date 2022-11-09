package com.example.lib;

public interface Engine {
    //Main functions
    public IGraphics getGraphics();
    public IAudio getAudio();
    public Input getInput();
    public IEventHandler getEventMngr();

    public int getWidth();
    public int getHeight();

    //Other functions(customized)
    public void paintCell(int x,int y, int w,int h, int typeAux);
    public void drawText(String text, int x, int y, String color,String fontName);
    public void drawCircle(float x, float y, float r ,String color);
    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, String imageName);
//    public void addComponent(Component aux);
    public void setScene(Scene newScene);
    public void setSceneMngr(ISceneMngr sceneMngr);
    public void popScene();
}