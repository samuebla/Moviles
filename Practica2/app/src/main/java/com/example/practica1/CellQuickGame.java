package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.Vector2D;

public class CellQuickGame extends CellBase {

    //Para el contador de celdas restantes y erroneas
    int key;

    public CellQuickGame(int x, int y, int width, int height) {
        this.setSize(width, height);
        this.setPos(x, y);
        this.type = cellType.EMPTY;
        key = -1;
    }

    @Override
    public void handleInput(EngineApp engine) {
        switch (type) {
            case EMPTY:
                type = cellType.SELECTED;

                //1 si la seleccionas y no es la solucion
                if (!solution) {
                    key = 1;
                }
                //2 si lo seleccionas y es la solucion
                else {
                    key = 2;
                }
                break;
            case SELECTED:
                type = cellType.CROSSED;
                //3 si lo tenias seleccionado y estaba mal pero ahora no
                if (!solution) {
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

    //1 Si lo seleccionas y esta mal
    //2 Si lo seleccionas y esta bien
    //3 Si estaba mal seleccionado y lo deseleccionas
    //4 Si estaba bien seleccionado y lo deseleccionas
    //5 Si lo has seleccionado y no puedes interactuar más
    //6 SI ES UN EMPTY y es WRONG
    //7 Si es un empty y es Selected
    public int keyCell() {
        return key;
    }

    //Para el boton de comprobar
    public void trueRender(EngineApp engine) {
        //Si te has equivocado...
        if (key == 1) {
            //Renderizamos a rojo
            engine.paintCell((int) this.getPos().getX(), (int) this.getPos().getY(), (int) this.getSize().getX(), (int) this.getSize().getY(), cellType.WRONG.ordinal());
        } else {
            engine.paintCell((int) this.getPos().getX(), (int) this.getPos().getY(), (int) this.getSize().getX(), (int) this.getSize().getY(), type.ordinal());
        }
    }

    public void changeEmptyCells() {
        if (key == 1) {
            key = -1;
            type = cellType.EMPTY;
        }
    }
}
