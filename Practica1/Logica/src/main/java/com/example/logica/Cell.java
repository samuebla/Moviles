package com.example.logica;

import com.example.lib.Engine;

import java.awt.geom.Point2D;

//Struct
public class Cell extends Interactive{

    public enum cellType {EMPTY,SELECTED,CROSSED,WRONG};

    //PROVISIONAL
    //private String color;

    private cellType type;
    private boolean solution = false;


    public Cell(int x, int y, int width, int height) {
        this.setSize(width,height);
        this.setPos(x,y);
        type = cellType.EMPTY;
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(Engine engine) {
        engine.paintCell((int)this.getPos().getX(), (int)this.getPos().getY(), (int)this.getSize().getX(), (int)this.getSize().getY(), type.ordinal());
    }

    @Override
    public void handleInput() {
        switch(type){
            case EMPTY:
                type = cellType.SELECTED;
                break;
            case SELECTED:
                type = cellType.CROSSED;
                break;
            case CROSSED:
                type = cellType.EMPTY;
                break;
        }
    }

    @Override
    public Point2D getSize() {
        return size;
    }

    @Override
    public Point2D getPos() {
        return pos;
    }

    //PROVISIONAL
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
