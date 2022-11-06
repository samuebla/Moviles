package com.example.lib;


import java.awt.geom.Point2D;

public interface Input {
    public Point2D getRawCoords();
    public void setRawCoords(int x, int y);
}
