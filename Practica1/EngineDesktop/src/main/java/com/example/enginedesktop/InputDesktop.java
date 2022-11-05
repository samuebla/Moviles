package com.example.enginedesktop;

import com.example.lib.Input;
import com.sun.org.apache.bcel.internal.generic.NEW;

//Input
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputDesktop implements Input {
    public Coords mouseCoords;
    private MouseListener listener;

    public InputDesktop(){
        this.listener = new MouseListener();
    }

    @Override
    public void setCoords(int x, int y) {
        this.mouseCoords.x = x;
        this.mouseCoords.y = y;
    }

    @Override
    public Coords getRawCoords() {
        return this.mouseCoords;
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

    @Override
    public void mousePressed(MouseEvent e) {
        super.mouseClicked(e);
        if(InputEvent.BUTTON1_DOWN_MASK != 0){
            System.out.println("Click detected");
            mouse_isClicked = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseExited(e);
        if(mouse_isClicked) {
            System.out.println("Click released");
            mouse_isClicked = false;
        }
    }
}