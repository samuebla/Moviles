package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.Vector2D;


public class CellHistoryMode extends CellBase {

    public CellHistoryMode(int x, int y, int width, int height,cellType typeAux,boolean solutionAux) {
        this.setSize(width, height);
        this.setPos(x, y);
        this.type = typeAux;
        solution = solutionAux;
    }

    @Override
    public void handleInput(EngineApp engine) {
        //Solo puedes interaccionar si está empty
        if (type == CellBase.cellType.EMPTY) {
            //Si te has equivocado...
            if (!solution) {
                type = cellType.WRONG;
            }
            //Si acertaste...
            else {
                type = cellType.SELECTED;
            }
        }
    }

    public cellType getCellType() { return type;}
}
