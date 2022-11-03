package com.example.lib;

public interface IGraphics {
    //Main functions
//    public IImage newImage();
//    public IFont newFont();
    public void setResolution(double width,double height);
    public void setColor(int color);
    public void setFont(int size,int fontType);
    public void drawImage();
    public void drawRectangle(int x,int y,int w,int h);
    public void fillRectangle(int x,int y,int w,int h);
    public void drawLine(int x,int y,int w,int h);
    public void drawText(int x,int y,String text);

    //Other functions
    //Sacamos de aqui el ancho y alto de la pantalla???
}