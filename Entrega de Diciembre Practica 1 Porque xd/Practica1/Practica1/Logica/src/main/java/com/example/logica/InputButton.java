package com.example.logica;

import com.example.lib.Engine;
import com.example.lib.IGraphics;
import com.example.lib.Vector2D;

//Clase para todos los botones
public class InputButton extends Interactive {

    public InputButton(double posX, double posY, double sizeW, double sizeH){
        setPos((int)posX,(int)posY);
        setSize((int)sizeW,(int)sizeH);
    }

    @Override
    public void render(IGraphics render){

    }
    @Override
    public void update(double deltaTime){

    }
    @Override
    public void handleInput(){

    }

    @Override
    public void setSize(int w, int h){
        this.size.set(w,h);
    };
    @Override
    public void setPos(int x, int y){
        this.pos.set(x, y);
    };

    public Vector2D getPos(){
        return this.pos;
    }

    public Vector2D getSize(){
        return this.size;
    }
}
