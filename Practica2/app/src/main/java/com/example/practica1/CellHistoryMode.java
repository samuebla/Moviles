package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.Vector2D;

public class CellHistoryMode extends CellBase {

    public CellHistoryMode(int x, int y, int width, int height) {
        this.setSize(width, height);
        this.setPos(x, y);
        this.type = CellBase.cellType.EMPTY;
        key = -1;
    }

    @Override
    public void handleInput(EngineApp engine) {
        //Solo puedes interaccionar si está empty
        if (type == CellBase.cellType.EMPTY) {
            //Si te has equivocado...
            if (!solution) {
                key = 1;
                type = cellType.WRONG;

            }
            //Si acertaste...
            else {
                key = 2;
                type = cellType.SELECTED;
            }
        }
    }
}
