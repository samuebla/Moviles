package com.example.lib;

public interface IGraphics {
    //Main functions
    public IImage newImage();
    public IFont newFont();
    public void setResolution();
    public void setColor();
    public void setFont();
    public void drawImage();
    public void drawRectangle();
    public void fillRectangle();
    public void drawLine();
    public void drawText();

    //Other functions
}

interface IFont{
    public int getSize();
    public boolean isBold();
}

interface IImage{
    public int getWidth();
    public int getHeight();
}
