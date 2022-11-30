package com.example.engineandroid;

import android.view.View;
import android.view.MotionEvent;

public class InputAndroid {

    private TouchListener touchlistener;
    private MotionListener motionlistener;
    private LongTouchListener longTouchListener;
    Vector2D touchCoords;
    private Vector2D offset;

    private float scaleFactor = 1.0f;

    public InputAndroid(EventHandler eHandler){
        this.touchCoords = new Vector2D(-1,-1);
        this.touchlistener = new TouchListener(this, eHandler);
        this.motionlistener = new MotionListener(this, eHandler);
        this.longTouchListener = new LongTouchListener(this, eHandler, this.touchlistener);
        offset = new Vector2D();
    }

    public Vector2D getRawCoords() {
        return new Vector2D(this.touchCoords.getX(), this.touchCoords.getY());
    }

    public Vector2D getScaledCoords() {
        return new Vector2D((getRawCoords().getX() - offset.getX())/scaleFactor, (getRawCoords().getY()  - offset.getY())/scaleFactor);
    }

    public void setRawCoords(int x, int y) {
        this.touchCoords.set(x, y);
    }

    public void setScaleFactor(float scale) {
        this.scaleFactor = scale;
    }

    public void setOffset(float x, float y) {
        this.offset.set(x,y);
    }

    public TouchListener getTouchListener(){
        return this.touchlistener;
    }
    public MotionListener getMotionListener(){
        return this.motionlistener;
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
        return false;
    }

    public boolean processEvent(MotionEvent e){
        if(e.getAction() == MotionEvent.ACTION_UP){
            //Para que no se haga simple click despues de long touch
            if (cancel){
                cancel = false;
                return false;
            }
            this.inputAndroid.setRawCoords((int)e.getX(),(int)e.getY());
            System.out.println("Click detected "+ "[X] " + this.inputAndroid.touchCoords.getX() + "[Y] " + this.inputAndroid.touchCoords.getY());
            this.eventHandler.sendEvent(EventHandler.EventType.TOUCH);
            return true;
        }
        return false;
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

class MotionListener implements View.OnGenericMotionListener {
    InputAndroid inputAndroid;
    EventHandler eventHandler;

    public MotionListener(InputAndroid iAndroid, EventHandler eHandler){
        this.inputAndroid = iAndroid;
        this.eventHandler = eHandler;
    }

    @Override
    public boolean onGenericMotion(View view, MotionEvent motionEvent) {
        processMotion(motionEvent);
        return true;
    }

    public void processMotion(MotionEvent e){
        this.inputAndroid.setRawCoords((int)e.getX(),(int)e.getY());
//        if(e.getAction() == MotionEvent.ACTION_DOWN){
//            this.inputAndroid.setRawCoords((int)e.getX(),(int)e.getY());
//            System.out.println("Click detected "+ "[X] " + this.inputAndroid.touchCoords.getX() + "[Y] " + this.inputAndroid.touchCoords.getY());
//            this.eventHandler.sendEvent(EventHandler.EventType.TOUCH);
//        }
    }
}