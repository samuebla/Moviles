package com.example.logica;

import com.example.lib.Engine;
import com.example.lib.Vector2D;

import java.awt.geom.Point2D;

//Struct
public class Cell extends Interactive {

    public enum cellType {EMPTY, SELECTED, CROSSED, WRONG}

    ;

    //PROVISIONAL
    //private String color;

    private cellType type;
    private boolean solution = false;


    public Cell(int x, int y, int width, int height) {
        this.setSize(width, height);
        this.setPos(x, y);
        type = cellType.EMPTY;
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

    public void setColor(cellType color) {
        this.type = color;
    }

    //-1 Si te equivocaste
    //1 Si acertaste
    //0 Si es vacio
    public int checkSolution() {
        if (solution && type == cellType.SELECTED) {
            //Le decimos que tenia razon
            return 1;
        }
        //Si no es la solucion y lo tienes seleccionado...
        else if (!solution && type == cellType.SELECTED) {
            //Cambias el render y le dices que se ha equivocado
            type = cellType.WRONG;
            return -1;
        }
        return 0;
    }
}
