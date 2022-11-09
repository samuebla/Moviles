package com.example.logica;

import com.example.lib.Engine;
import com.example.lib.Vector2D;

//Struct
public class Cell extends Interactive {

    public enum cellType {EMPTY, SELECTED, CROSSED, WRONG};

    private cellType type;
    private boolean solution = false;

    //Para el contador de celdas restantes y erroneas
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
    public void handleInput(Engine engine) {
        switch (type) {
            case EMPTY:
                type = cellType.SELECTED;

                //1 si la seleccionas y no es la solucion
                if (!solution) {
                    key = 1;
                }
                //2 si lo seleccionas y es la solucion
                else{
                    key = 2;
                }
                break;
            case SELECTED:
                type = cellType.CROSSED;

                //3 si lo tenias seleccionado y estaba mal pero ahora no
                if(!solution){
                    key = 3;
                }
                //4 si estaba bien seleccionado y lo deseleccionas
                else {
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

    //Para el boton de comprobar
    public void trueRender(Engine engine) {
        //Si te has equivocado...
        if(key == 1){
            //Renderizamos a rojo
            engine.paintCell((int) this.getPos().getX(), (int) this.getPos().getY(), (int) this.getSize().getX(), (int) this.getSize().getY(), cellType.WRONG.ordinal());
            //Pero cambiamos a empty despues
            type = cellType.EMPTY;
        }
        else{
            engine.paintCell((int) this.getPos().getX(), (int) this.getPos().getY(), (int) this.getSize().getX(), (int) this.getSize().getY(), type.ordinal());
        }
    }

    //Para la pantalla de Enhorabuena
    public void solutionRender(Engine engine) {
        //Solo renderizo si esta azul
        if(type == cellType.SELECTED){
            engine.paintCell((int) this.getPos().getX(), (int) this.getPos().getY(), (int) this.getSize().getX(), (int) this.getSize().getY(), type.ordinal());
        }
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
