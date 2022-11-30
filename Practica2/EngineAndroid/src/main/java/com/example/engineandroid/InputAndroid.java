package com.example.engineandroid;

import android.view.View;
import android.view.MotionEvent;

public class InputAndroid {

    private TouchListener touchlistener;
    private MotionListener motionlistener;
    Vector2D touchCoords;
    private Vector2D offset;

    private float scaleFactor = 1.0f;

    public InputAndroid(EventHandler eHandler){
        this.touchCoords = new Vector2D(-1,-1);
        this.touchlistener = new TouchListener(this, eHandler);
        this.motionlistener = new MotionListener(this, eHandler);
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
}

class TouchListener implements View.OnTouchListener {
    InputAndroid inputAndroid;
    EventHandler eventHandler;

    public TouchListener(InputAndroid iAndroid, EventHandler eHandler){
        this.inputAndroid = iAndroid;
        this.eventHandler = eHandler;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        processEvent(motionEvent);
        return false;
    }

    public void processEvent(MotionEvent e){
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            this.inputAndroid.setRawCoords((int)e.getX(),(int)e.getY());
            System.out.println("Click detected "+ "[X] " + this.inputAndroid.touchCoords.getX() + "[Y] " + this.inputAndroid.touchCoords.getY());
            this.eventHandler.sendEvent(EventHandler.EventType.TOUCH);
        }
        else if(e.getAction() == MotionEvent.ACTION_UP){
            this.inputAndroid.setRawCoords((int)e.getX(),(int)e.getY());
            System.out.println("Click detected "+ "[X] " + this.inputAndroid.touchCoords.getX() + "[Y] " + this.inputAndroid.touchCoords.getY());
            this.eventHandler.sendEvent(EventHandler.EventType.RELEASE);
        }
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
//        if(e.getAction() == MotionEvent.ACTION_DOWN){
//            this.inputAndroid.setRawCoords((int)e.getX(),(int)e.getY());
//            System.out.println("Click detected "+ "[X] " + this.inputAndroid.touchCoords.getX() + "[Y] " + this.inputAndroid.touchCoords.getY());
//            this.eventHandler.sendEvent(EventHandler.EventType.TOUCH);
//        }
    }
}