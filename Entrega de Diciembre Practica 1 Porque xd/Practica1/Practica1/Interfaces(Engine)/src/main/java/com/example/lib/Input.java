package com.example.lib;


public interface Input {
    Vector2D getRawCoords();
    Vector2D getScaledCoords(Vector2D coords);
    void setRawCoords(float x, float y);
    //Pos y tamaño del objeto Input a recibir
    boolean InputReceive(Vector2D pos, Vector2D size);
}
