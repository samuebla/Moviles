package com.example.enginedesktop;

import com.example.lib.Input;
import com.example.lib.IEventHandler;
import com.example.lib.Vector2D;

//Input
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputDesktop implements Input {
    private final MouseListener listener;

    Vector2D mouseCoords;

    private final RenderDesktop render;

    public InputDesktop(RenderDesktop rend, EventHandlerDesktop eHandler){
        this.mouseCoords = new Vector2D();
        this.listener = new MouseListener(this, eHandler);
        render = rend;
    }

    @Override
    public Vector2D getRawCoords() {
        return this.mouseCoords;
    }

    @Override
    public Vector2D getScaledCoords(Vector2D coords) {
        return new Vector2D(coords.getX(), coords.getY());
    }

    @Override
    public void setRawCoords(float x, float y) {
        this.mouseCoords.set(x,y);
    }

    public MouseListener getListener(){
        return this.listener;
    }

    public boolean mouse_Clicked(){
        return  this.listener.mouse_isClicked;
    }

    public boolean InputReceive(Vector2D pos, Vector2D size){
        if(mouseCoords.getX() == 0 && mouseCoords.getY()==0)
            return false;

        Vector2D coords = new Vector2D(getRawCoords());
        Vector2D posScaled = render.convertLogicCoordsToWindow(pos);
        Vector2D sizeScaled = render.convertLogicSizeToWindow(size);
        System.out.println("Object Pos [X] " + posScaled.getX() + "[Y] " + posScaled.getY() + ",Size: [X] " + sizeScaled.getX() + "[Y] " + sizeScaled.getY());
        return (coords.getX() >= posScaled.getX() && coords.getX() <= posScaled.getX() + sizeScaled.getX() &&
                coords.getY() >= posScaled.getY() && coords.getY() <= posScaled.getY() + sizeScaled.getY());
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
            Vector2D coords = new Vector2D(inputDesktop.getRawCoords());
            System.out.println("Click detected Raw"+ "[X] " + coords.getX() + "[Y] " + coords.getY());
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