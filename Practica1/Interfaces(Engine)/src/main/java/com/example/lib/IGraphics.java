package com.example.lib;

public interface IGraphics {
    //Main functions
    public IImage newImage(String imageName,String path);
    public IFont newFont(String fontName,String path,int type,int size);
    public void setResolution(double width,double height);
    public int getWidth();
    public int getHeight();
    public void setColor(int color);
    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, String imageName);
    public void drawRectangle(int x,int y,int w,int h, boolean fill);
    public void drawLine(int x,int y,int w,int h);
    public void drawText(String text, int x, int y, String color,String fontKey);
    public void drawCircle(float x, float y, float r ,String color);
}