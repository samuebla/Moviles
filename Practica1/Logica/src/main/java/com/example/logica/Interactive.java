package com.example.logica;

import com.example.lib.Engine;
import com.example.lib.Vector2D;

import java.awt.geom.Point2D;

public class Interactive {
    Vector2D size = new Vector2D();
    Vector2D pos = new Vector2D();

    public void render(Engine engine){};
    public void update(double deltaTime){};
    public void handleInput(){};

    public Vector2D getSize(){return size;};
    public Vector2D getPos(){return pos;};

    public void setSize(int w, int h){
        this.size.set(w,h);
    };
    public void setPos(int x, int y){
        this.pos.set(x, y);
    };
}
