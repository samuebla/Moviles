package com.example.logica;

import com.example.engineandroid.*;

//Clase Abstracta para todos los elementos que tengan Input
public class Interactive {
    Vector2D size = new Vector2D();
    Vector2D pos = new Vector2D();

    public void render(EngineApp engine){};
    public void update(double deltaTime){};
    public void handleInput(EngineApp engine){};

    public Vector2D getSize(){return size;};
    public Vector2D getPos(){return pos;};

    public void setSize(int w, int h){
        this.size.set(w,h);
    };
    public void setPos(int x, int y){
        this.pos.set(x, y);
    };
}
