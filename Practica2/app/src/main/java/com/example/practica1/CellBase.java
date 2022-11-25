package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.Vector2D;

public class CellBase extends Interactive{

    cellType type;
    boolean solution = false;

    //Para el contador de celdas restantes y erroneas
    int key;

    public enum cellType {EMPTY, SELECTED, CROSSED, WRONG}

    public void render(EngineApp engine){
        engine.paintCell((int) this.getPos().getX(), (int) this.getPos().getY(), (int) this.getSize().getX(), (int) this.getSize().getY(), type.ordinal());
    };

    //Para la pantalla de Enhorabuena
    public void solutionRender(EngineApp engine) {
        //Solo renderizo si esta azul
        if (type == CellBase.cellType.SELECTED) {
            engine.paintCell((int) this.getPos().getX(), (int) this.getPos().getY(), (int) this.getSize().getX(), (int) this.getSize().getY(), type.ordinal());
        }
    }

    public void handleInput(EngineApp engine){};

    public void setSolution(boolean aux) {
        solution = aux;
    }

    public boolean getSolution() {
        return solution;
    }
    //1 Si esta mal
    //2 Si lo seleccionas y esta bien
    //3 Si estaba mal seleccionado y lo deseleccionas
    //4 Si estaba bien seleccionado y lo deseleccionas
    //5 Si lo has seleccionado y no puedes interactuar más

    public int keyCell() {
        return key;
    }

    //POSICIONES
    public Vector2D getSize(){return size;};
    public Vector2D getPos(){return pos;};

    public void setSize(int w, int h){
        this.size.set(w,h);
    };
    public void setPos(int x, int y){
        this.pos.set(x, y);
    };

}
