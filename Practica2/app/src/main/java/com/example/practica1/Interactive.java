package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.RenderAndroid;
import com.example.engineandroid.Vector2D;

//Clase Abstracta para todos los elementos que tengan Input
public class Interactive {
    Vector2D size = new Vector2D();
    Vector2D pos = new Vector2D();

    public void render(RenderAndroid render){}
    public void update(double deltaTime){}
    public void handleInput(){}

    public Vector2D getSize(){return size;}
    public Vector2D getPos(){return pos;}

    public void setSize(int w, int h){
        this.size.set(w,h);
    }
    public void setPos(int x, int y){
        this.pos.set(x, y);
    }
}
