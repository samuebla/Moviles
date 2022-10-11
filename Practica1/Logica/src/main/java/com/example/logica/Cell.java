package com.example.logica;

import com.example.lib.Engine;

//Struct
public class Cell{

    public enum cellType {EMPTY,SELECTED,CROSSED,WRONG};

    private float x1;
    private float y1;
    private float x2;
    private float y2;
    //PROVISIONAL
    //private String color;

    private cellType type;
    private boolean solution = false;


    public Cell(float x1aux, float y1aux, float x2aux, float y2aux) {
        this.x1 = x1aux;
        this.y1 = y1aux;
        this.x2 = x2aux;
        this.y2 = y2aux;
        type = cellType.EMPTY;
    }

    public void update(double deltaTime) {

    }

    public void render(Engine engine) {
        engine.paintCell(this.x1, this.y1, this.x2, this.y2, type);
    }

    //PROVISIONAL
    int size;

    public void setSize(int sizeAux) {
        size = sizeAux;
    }

    public int getSize() {
        return size;
    }


    public void setType(cellType aux) {
        type = aux;
    }

    public void setSolution(boolean aux) {
        solution = aux;
    }

    public cellType getType() {
        return type;
    }

    public boolean getSolution() {
        return solution;
    }

    public void setColor(cellType color) {
        this.type = color;
    }
}
