package com.example.lib;

public interface IGraphics {
    //Main functions
//    public IImage newImage();
//    public IFont newFont();
    public void setResolution(double width,double height);
    public void setColor();
    public void setFont();
    public void drawImage();
    public void drawRectangle();
    public void fillRectangle();
    public void drawLine();
    public void drawText();

    //Other functions
}