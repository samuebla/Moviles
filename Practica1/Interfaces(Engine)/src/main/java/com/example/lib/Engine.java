package com.example.lib;
import com.example.logica.Cell.cellType;

public interface Engine {
    //Main functions
    public IGraphics getGraphics();
    public IAudio getAudio();
    public IState getState();

    //Other functions(customized)
    public void paintCell(float x1,float x2, float y1,float y2, cellType typeAux);
}