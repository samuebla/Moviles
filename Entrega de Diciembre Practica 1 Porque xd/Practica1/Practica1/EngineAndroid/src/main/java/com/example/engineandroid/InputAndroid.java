package com.example.engineandroid;

import android.view.View;
import android.view.MotionEvent;

import com.example.lib.IEventHandler;
import com.example.lib.Input;
import com.example.lib.Vector2D;

public class InputAndroid implements Input {

    private TouchListener touchlistener;
    private MotionListener motionlistener;
    Vector2D touchCoords;
    private Vector2D offset;

    private float scaleFactor = 1.0f;

    public InputAndroid(IEventHandler eHandler){
        this.touchCoords = new Vector2D(-1,-1);
        this.touchlistener = new TouchListener(this, eHandler);
        this.motionlistener = new MotionListener(this, eHandler);
        offset = new Vector2D();
    }

    @Override
    public Vector2D getRawCoords() {
        return new Vector2D(this.touchCoords.getX(), this.touchCoords.getY());
    }

    @Override
    public Vector2D getScaledCoords(Vector2D coords) {
        return new Vector2D((coords.getX() - offset.getX())*scaleFactor, (coords.getY()  - offset.getY())*scaleFactor);
    }

    @Override
    public void setRawCoords(int x, int y) {
        this.touchCoords.set(x, y);
    }

    @Override
    public boolean InputReceive(Vector2D pos, Vector2D size) {
        if(touchCoords.getX() == 0 && touchCoords.getY()==0)
            return false;

        Vector2D coords = new Vector2D(getRawCoords());
        Vector2D posScaled = new Vector2D((pos.getX())*scaleFactor+ offset.getX(), pos.getY()*scaleFactor + offset.getY());
        Vector2D sizeScaled = new Vector2D((size.getX())*scaleFactor , size.getY()*scaleFactor);

//        System.out.println("ScaleFactor: " + scaleFactor);
//        System.out.println("Margin [X] " + offset.getX() + "[Y] " + offset.getY());
//        System.out.println("Object Pos [X] " + posScaled.getX() + "[Y] " + posScaled.getY() + ",Size: [X] " + sizeScaled.getX() + "[Y] " + sizeScaled.getY());

        return (coords.getX() >= posScaled.getX() && coords.getX() <= posScaled.getX() + sizeScaled.getX() &&
                coords.getY() >= posScaled.getY() && coords.getY() <= posScaled.getY() + sizeScaled.getY());
    }

    @Override
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
    IEventHandler eventHandler;

    public TouchListener(InputAndroid iAndroid, IEventHandler eHandler){
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
            this.eventHandler.sendEvent(IEventHandler.EventType.TOUCH);
        }
    }
}

class MotionListener implements View.OnGenericMotionListener {
    InputAndroid inputAndroid;
    IEventHandler eventHandler;

    public MotionListener(InputAndroid iAndroid, IEventHandler eHandler){
        this.inputAndroid = iAndroid;
        this.eventHandler = eHandler;
    }

    @Override
    public boolean onGenericMotion(View view, MotionEvent motionEvent) {
        processMotion(motionEvent);
        return true;
    }

    public void processMotion(MotionEvent e){
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            this.inputAndroid.setRawCoords((int)e.getX(),(int)e.getY());
            System.out.println("Click detected "+ "[X] " + this.inputAndroid.touchCoords.getX() + "[Y] " + this.inputAndroid.touchCoords.getY());
            this.eventHandler.sendEvent(IEventHandler.EventType.TOUCH);
        }
    }
}