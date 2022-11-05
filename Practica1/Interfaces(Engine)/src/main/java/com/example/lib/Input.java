package com.example.lib;


public interface Input {
    class Coords{
        public int x;
        public int y;
    }
    public void setCoords(int x, int y);
    public Coords getRawCoords();

}
