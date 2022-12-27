package com.example.lib;

public interface IGraphics {
    //Main functions
    IImage newImage(String imageName,String path);
    IFont newFont(String fontName,String path,int type);
    void setResolution(double width,double height);
    int getWidth();
    int getHeight();
    void setColor(int color);
    void drawImage(int x, int y, int desiredWidth, int desiredHeight, String imageName);
    void drawRectangle(int x,int y,int w,int h, boolean fill);
    void drawLine(int x,int y,int w,int h);
    void drawText(String text, int x, int y, String color,String fontKey, int alignType, int size);
    void drawCircle(float x, float y, float r ,String color);
    void paintCell(int x, int y, int w, int h, int celltype);
}