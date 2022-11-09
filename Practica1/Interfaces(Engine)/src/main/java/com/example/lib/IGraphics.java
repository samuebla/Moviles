package com.example.lib;

public interface IGraphics {
    //Main functions
//    public IImage newImage();
//    public IFont newFont();
    public void setResolution(double width,double height);
    public int getWidth();
    public int getHeight();
    public void setColor(int color);
    public void setFont(int size,int fontType);
    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, IImage image);
    public void drawRectangle(int x,int y,int w,int h, boolean fill);
    public void fillRectangle(int x,int y,int w,int h);
    public void drawLine(int x,int y,int w,int h);
    public void drawText(String text, int x, int y, String color,IFont font);

    //Other functions
    //Sacamos de aqui el ancho y alto de la pantalla???
}