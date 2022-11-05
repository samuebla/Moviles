package com.example.logica;

import com.example.lib.Engine;

//Struct
public class Cell{

    public enum cellType {EMPTY,SELECTED,CROSSED,WRONG};

    private int x1;
    private int y1;
    private int w;
    private int h;
    //PROVISIONAL
    //private String color;

    private cellType type;
    private boolean solution = false;


    public Cell(int x, int y, int width, int height) {
        this.x1 = x;
        this.y1 = y;
        this.w = width;
        this.h = height;
        type = cellType.EMPTY;
    }

    public void update(double deltaTime) {

    }

    public void render(Engine engine) {
        engine.paintCell(this.x1, this.y1, this.w, this.h, type.ordinal());
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
//        if(solution){
//            type =cellType.SELECTED;
//        }
//        else{
//            type = cellType.EMPTY;
//        }
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
