package com.example.engineandroid;

import android.view.View;
import android.view.MotionEvent;

import com.example.lib.IEventHandler;
import com.example.lib.Input;
import com.example.lib.Vector2D;

public class InputAndroid implements Input {

    private final TouchListener touchlistener;
    Vector2D touchCoords;
    private final Vector2D offset;

    private float scaleFactor = 1.0f;

    private RenderAndroid render;

    public InputAndroid(IEventHandler eHandler, RenderAndroid render){
        this.render = render;
        this.touchCoords = new Vector2D(-1,-1);
        //Touch listener llamado cuando el view detecta input
        this.touchlistener = new TouchListener(this, eHandler);
        offset = new Vector2D();
    }

    @Override
    public Vector2D getRawCoords() {
        return new Vector2D(this.touchCoords.getX(), this.touchCoords.getY());
    }

    @Override
    public Vector2D getScaledCoords(Vector2D coords) {
        return new Vector2D(coords.getX(), coords.getY());
    }

    @Override
    public void setRawCoords(float x, float y) {
        this.touchCoords.set(x, y);
    }

    //Comprueba si el toque en la pantalla esta dentro de las coordenadas definidas por pos y size
    @Override
    public boolean InputReceive(Vector2D pos, Vector2D size) {
        if(touchCoords.getX() == 0 && touchCoords.getY()==0)
            return false;

        Vector2D coords = new Vector2D(getRawCoords());
        Vector2D posScaled = render.convertLogicCoordsToView(pos);
        Vector2D sizeScaled = render.convertLogicSizeToView(size);

//        System.out.println("ScaleFactor: " + scaleFactor);
//        System.out.println("Margin [X] " + offset.getX() + "[Y] " + offset.getY());
//        System.out.println("Object Pos [X] " + posScaled.getX() + "[Y] " + posScaled.getY() + ",Size: [X] " + sizeScaled.getX() + "[Y] " + sizeScaled.getY());

        return (coords.getX() >= posScaled.getX() && coords.getX() <= posScaled.getX() + sizeScaled.getX() &&
                coords.getY() >= posScaled.getY() && coords.getY() <= posScaled.getY() + sizeScaled.getY());
    }

    public TouchListener getTouchListener(){
        return this.touchlistener;
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
        view.performClick();
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