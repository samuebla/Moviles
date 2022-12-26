package com.example.lib;


public interface Input {
    public Vector2D getRawCoords();
    public Vector2D getScaledCoords(Vector2D coords);
    public void setRawCoords(int x, int y);
    //Pos y tamaño del objeto Input a recibir
    public boolean InputReceive(Vector2D pos, Vector2D size);

    public void setScaleFactor(float scale);
}
