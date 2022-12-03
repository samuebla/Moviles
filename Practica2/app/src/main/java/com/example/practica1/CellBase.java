package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.Vector2D;

public class CellBase extends Interactive{

    cellType type;
    boolean solution = false;

    int palleteColor = -1;
    public enum cellType {EMPTY, SELECTED, CROSSED, WRONG}

    public void render(EngineApp engine){
        engine.paintCell((int) this.getPos().getX(), (int) this.getPos().getY(), (int) this.getSize().getX(), (int) this.getSize().getY(), type.ordinal(),palleteColor);
    };

    //Para la pantalla de Enhorabuena
    public void solutionRender(EngineApp engine) {
        //Solo renderizo si esta azul
        if (type == CellBase.cellType.SELECTED) {
            engine.paintCell((int) this.getPos().getX(), (int) this.getPos().getY(), (int) this.getSize().getX(), (int) this.getSize().getY(), type.ordinal(),palleteColor);
        }
    }

    public void handleInput(EngineApp engine){};

    public void setPalleteColor(int pallet) { palleteColor = pallet; }
    
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
