package com.example.logica;

import com.example.lib.Engine;
import com.example.lib.Vector2D;

import java.awt.geom.Point2D;

//Struct
public class Cell extends Interactive {

    public enum cellType {EMPTY, SELECTED, CROSSED, WRONG}

    ;

    private cellType type;
    private boolean solution = false;
    int key;


    public Cell(int x, int y, int width, int height) {
        this.setSize(width, height);
        this.setPos(x, y);
        type = cellType.EMPTY;
        key = -1;
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(Engine engine) {
        engine.paintCell((int) this.getPos().getX(), (int) this.getPos().getY(), (int) this.getSize().getX(), (int) this.getSize().getY(), type.ordinal());
    }

    @Override
    public void handleInput() {
        switch (type) {
            case EMPTY:
                type = cellType.SELECTED;
                //Si la seleccionas y no es la solucion
                if (!solution) {
                    //Lo guardamos
                    key = 1;
                }
                else{
                    key = 2;
                }
                break;
            case SELECTED:
                type = cellType.CROSSED;
                //Si lo tenias seleccionado y estaba mal...
                if(!solution){
                    //Deja de estar mal
                    key = 3;
                }
                //Si estaba bien...
                else {
                    //Ahora deja de estarlo
                    key = 4;
                }
                break;
            case CROSSED:
                type = cellType.EMPTY;
                key = 0;
                break;
        }
    }

    @Override
    public Vector2D getSize() {
        return size;
    }

    @Override
    public Vector2D getPos() {
        return pos;
    }

    //PROVISIONAL
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

    //1 Si esta mal
    //2 Si lo seleccionas y esta bien
    //3 Si estaba mal seleccionado y lo deseleccionas
    //4 Si estaba bien seleccionado y lo deseleccionas
    public int keyCell() {
        return key;
    }

    public void setColor(cellType color) {
        this.type = color;
    }
}
