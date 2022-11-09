package com.example.lib;


public interface Input {
    public Vector2D getRawCoords();
    public Vector2D getScaledCoords();
    public void setRawCoords(int x, int y);

    public void setScaleFactor(float scale);
}
