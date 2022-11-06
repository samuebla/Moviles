package com.example.logica;

import com.example.lib.Engine;

import java.awt.geom.Point2D;

public class Button extends Interactive {

    public Button(double posX, double posY, double sizeW, double sizeH){
        this.pos = new Point2D.Double(posX, posY);
        this.size = new Point2D.Double(sizeW, sizeH);
    }

    @Override
    public void render(Engine engine){

    }
    @Override
    public void update(double deltaTime){

    }
    @Override
    public void handleInput(){

    }

    @Override
    public void setSize(int w, int h){
        this.size.setLocation(w,h);
    };
    @Override
    public void setPos(int x, int y){
        this.pos.setLocation(x, y);
    };

    public Point2D getPos(){
        return this.pos;
    }

    public Point2D getSize(){
        return this.size;
    }
}
