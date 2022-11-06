package com.example.logica;

import com.example.lib.Engine;

import java.awt.geom.Point2D;

public class Interactive {
    Point2D size = new Point2D.Double();
    Point2D pos = new Point2D.Double();

    public void render(Engine engine){};
    public void update(double deltaTime){};
    public void handleInput(){};

    public Point2D getSize(){return size;};
    public Point2D getPos(){return pos;};

    public void setSize(int w, int h){
        this.size.setLocation(w,h);
    };
    public void setPos(int x, int y){
        this.pos.setLocation(x, y);
    };
}
