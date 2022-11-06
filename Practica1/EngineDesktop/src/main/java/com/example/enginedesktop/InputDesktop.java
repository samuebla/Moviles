package com.example.enginedesktop;

import com.example.lib.Input;
import com.example.lib.IEventHandler;
import com.sun.org.apache.bcel.internal.generic.NEW;

//Input
import java.awt.Event;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class InputDesktop implements Input {
    private MouseListener listener;

    Point2D mouseCoords;

    public InputDesktop(IEventHandler eHandler){
        this.mouseCoords = new Point2D.Double();
        this.listener = new MouseListener(this, eHandler);
    }

    @Override
    public Point2D getRawCoords() {
        return this.mouseCoords;
    }

    @Override
    public void setRawCoords(int x, int y) {
        this.mouseCoords.setLocation(x,y);
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