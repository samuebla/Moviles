package com.example.practica1;

import com.example.engineandroid.EngineApp;


public class CellHistoryMode extends CellBase {

    public CellHistoryMode(int x, int y, int width, int height,cellType typeAux,boolean solutionAux) {
        this.setSize(width, height);
        this.setPos(x, y);
        this.type = typeAux;
        solution = solutionAux;
    }

    @Override
    public void handleInput() {
        //Solo puedes interaccionar si está empty
        if (type == CellBase.cellType.EMPTY ||type == cellType.CROSSED ) {
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

    public void setCrossed(){
        System.out.println("A vel a vel: " + type.ordinal());
        if (type == CellBase.cellType.EMPTY) {
            System.out.println("a pues si");
            type = cellType.CROSSED;
        }
    }

    public cellType getCellType() { return type;}
}
