package com.example.practica1;

import com.example.engineandroid.RenderAndroid;
import com.example.engineandroid.Vector2D;

//Clase para todos los botones
public class InputButton extends Interactive {

    public InputButton(double posX, double posY, double sizeW, double sizeH){
        setPos((int)posX,(int)posY);
        setSize((int)sizeW,(int)sizeH);
    }

    @Override
    public void render(RenderAndroid render){

    }
    @Override
    public void update(double deltaTime){

    }
    @Override
    public void handleInput(){

    }

    @Override
    public void setSize(int w, int h){
        super.setSize(w,h);
    }
    @Override
    public void setPos(int x, int y){
        super.setPos(x, y);
    }

    public Vector2D getPos(){
        return super.getPos();
    }

    public Vector2D getSize(){
        return super.getSize();
    }
}
