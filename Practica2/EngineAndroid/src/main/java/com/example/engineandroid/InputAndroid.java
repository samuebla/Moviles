package com.example.engineandroid;

import android.view.View;
import android.view.MotionEvent;

public class InputAndroid {

    //TODO Aqui guarda la relacion
    int scaleWidth, scaleHeight;

    private final TouchListener touchlistener;
    private final LongTouchListener longTouchListener;
    Vector2D touchCoords;
    private final Vector2D offset;

    public InputAndroid(EventHandler eHandler){
        this.touchCoords = new Vector2D(-1,-1);
        this.touchlistener = new TouchListener(this, eHandler);
        this.longTouchListener = new LongTouchListener(this, eHandler, this.touchlistener);
        offset = new Vector2D();

        //Por defecto la escala es 1000x1000 pero creamos un setter por si alguien quiere alguna modificacion
        scaleHeight=1000;
        scaleWidth=1000;
    }

    public Vector2D getRawCoords() {
        return new Vector2D(this.touchCoords.getX(), this.touchCoords.getY());
    }

    public Vector2D getScaledCoords() {
        return new Vector2D((getRawCoords().getX() - offset.getX()), (getRawCoords().getY()  - offset.getY()));
    }

    public void setRawCoords(int x, int y) {
        this.touchCoords.set(x, y);
    }

    public void setOffset(float x, float y) {
        this.offset.set(x,y);
    }

    public TouchListener getTouchListener(){
        return this.touchlistener;
    }
    public LongTouchListener getLongTouchListener() { return this.longTouchListener; }
}

class TouchListener implements View.OnTouchListener {
    InputAndroid inputAndroid;
    EventHandler eventHandler;
    boolean cancel = false;

    public TouchListener(InputAndroid iAndroid, EventHandler eHandler){
        this.inputAndroid = iAndroid;
        this.eventHandler = eHandler;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        processEvent(motionEvent);
        view.performClick();
        return false;
    }

    public void processEvent(MotionEvent e){
        this.inputAndroid.setRawCoords((int)e.getX(),(int)e.getY());

        if(e.getAction() == MotionEvent.ACTION_UP){
            //Para que no se haga simple click despues de long touch
            if (cancel){
                cancel = false;
                return;
            }
            System.out.println("Click detected "+ "[X] " + this.inputAndroid.touchCoords.getX() + "[Y] " + this.inputAndroid.touchCoords.getY());
            this.eventHandler.sendEvent(EventHandler.EventType.TOUCH);
        }
    }

    public void setCancel(){
        cancel = true;
    }
}

class LongTouchListener implements View.OnLongClickListener{
    InputAndroid inputAndroid;
    EventHandler eventHandler;
    TouchListener touchListener;

    public LongTouchListener(InputAndroid iAndroid, EventHandler eHandler, TouchListener touch){
        this.inputAndroid = iAndroid;
        this.eventHandler = eHandler;
        this.touchListener = touch;
    }

    @Override
    public boolean onLongClick(View view) {
        this.touchListener.setCancel();
        processEvent();
        return true;
    }

    public void processEvent(){
        System.out.println("Long touch detected "+ "[X] " + this.inputAndroid.touchCoords.getX() + "[Y] " + this.inputAndroid.touchCoords.getY());
        this.eventHandler.sendEvent(EventHandler.EventType.LONG_TOUCH);
    }
}