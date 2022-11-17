package com.example.logica;
import com.example.engineandroid.*;

//Clase para todos los botones
public class Button extends Interactive {

    public Button(double posX, double posY, double sizeW, double sizeH){
        this.pos = new Vector2D(posX, posY);
        this.size = new Vector2D(sizeW, sizeH);
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
