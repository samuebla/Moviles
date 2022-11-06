package com.example.engineandroid;

import android.view.View;
import android.view.MotionEvent;

import com.example.lib.IEventHandler;
import com.example.lib.Input;
import com.example.lib.Vector2D;

public class InputAndroid implements Input {

    private MotionListener listener;
    Vector2D touchCoords;

    public InputAndroid(IEventHandler eHandler){
        this.touchCoords = new Vector2D();
        this.listener = new MotionListener(this, eHandler);
    }

    @Override
    public Vector2D getRawCoords() {

        return touchCoords;
    }

    @Override
    public void setRawCoords(int x, int y) {
        touchCoords.set(x, y);
    }

    public MotionListener getListener(){
        return this.listener;
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
        if(motionEvent == null) return false;

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


//class TouchListener implements View.OnTouchListener {
//
//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        return false;
//    }
//}
