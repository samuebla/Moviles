package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.Vector2D;

//Clase para todos los botones
public class Button extends Interactive {

    public Button(double posX, double posY, double sizeW, double sizeH){
        setPos((int)posX,(int)posY);
        setSize((int)sizeW,(int)sizeH);
    }

    @Override
    public void render(EngineApp engine){

    }
    @Override
    public void update(double deltaTime){

    }
    @Override
    public void handleInput(EngineApp engine){

    }

    @Override
    public void setSize(int w, int h){
        setSize(w,h);
    };
    @Override
    public void setPos(int x, int y){
        setPos(x, y);
    };

    public Vector2D getPos(){
        return getPos();
    }

    public Vector2D getSize(){
        return getSize();
    }
}
