package com.example.enginedesktop;

import com.example.lib.Input;
import com.example.lib.IEventHandler;
import com.example.lib.Vector2D;
import com.sun.org.apache.bcel.internal.generic.NEW;

//Input
import java.awt.Event;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputDesktop implements Input {
    private MouseListener listener;

    Vector2D mouseCoords;

    private float scaleFactor;
    private Vector2D offset;

    public InputDesktop(IEventHandler eHandler){
        this.mouseCoords = new Vector2D(0,0);
        this.listener = new MouseListener(this, eHandler);
        offset = new Vector2D();
    }

    @Override
    public Vector2D getRawCoords() {
        return this.mouseCoords;
    }

    @Override
    public Vector2D getScaledCoords() {
//        System.out.println("Mi abuela mide : [x]" + offset.getX() + " [y] " + offset.getY());
        return new Vector2D((getRawCoords().getX() - offset.getX())/scaleFactor, (getRawCoords().getY()  - offset.getY())/scaleFactor);
    }

    @Override
    public void setRawCoords(int x, int y) {
        this.mouseCoords.set(x,y);
    }

    @Override
    public void setScaleFactor(float scale) {
        this.scaleFactor = scale;
    }

    public void setOffset(Vector2D v) {
        this.offset = v;
    }

    public MouseListener getListener(){
        return this.listener;
    }

    public boolean mouse_Clicked(){
        return  this.listener.mouse_isClicked;
    }
}

class MouseListener extends MouseAdapter{
    public boolean mouse_isClicked = false;

    InputDesktop inputDesktop;
    IEventHandler eventHandler;

    public MouseListener(InputDesktop iDesktop, IEventHandler eHandler){
        this.inputDesktop = iDesktop;
        this.eventHandler = eHandler;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mouseClicked(e);
        if(InputEvent.BUTTON1_DOWN_MASK != 0){
            this.inputDesktop.setRawCoords((int)e.getPoint().getX(),(int)e.getPoint().getY());
            System.out.println("Click detected "+ "[X] " + this.inputDesktop.mouseCoords.getX() + "[Y] " + this.inputDesktop.mouseCoords.getY());
            this.mouse_isClicked = true;
            this.eventHandler.sendEvent(IEventHandler.EventType.MOUSE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseExited(e);
        if(this.mouse_isClicked) {
            System.out.println("Click released");
            this.mouse_isClicked = false;
        }
    }

}