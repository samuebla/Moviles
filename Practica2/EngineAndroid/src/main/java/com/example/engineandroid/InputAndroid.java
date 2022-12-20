package com.example.engineandroid;

import android.view.View;
import android.view.MotionEvent;

public class InputAndroid {

    //Maxima escala de la logica, por defecto: 1000, 1000.
    //Siendo 0 la izquierda del surface y 1000 la derecha del surface para scaleWidth.
    //Y 0 arriba del surface y 1000 abajo del surface para scaleHeight.
    int scaleWidth, scaleHeight;

    //Listeners para los clicks
    private final TouchListener touchlistener;
    private final LongTouchListener longTouchListener;
    //Donde se ha pulsado por ultima vez
    Vector2D touchCoords;

    //Referencia al render para obtener el tamaño del surface
    private final RenderAndroid render;

    public InputAndroid(EventHandler eHandler, RenderAndroid render){
        this.touchCoords = new Vector2D(-1,-1);
        this.touchlistener = new TouchListener(this, eHandler);
        this.longTouchListener = new LongTouchListener(this, eHandler, this.touchlistener);

        this.render = render;

        //Por defecto la escala es 1000x1000
        scaleHeight=1000;
        scaleWidth=1000;
    }

    //Actualmente Pos y Size se devuelve en unidades de 0 a 1000 pero el getScaledCoords esta en cordenadas reales por eso hago la conversion
    public boolean inputReceived(Vector2D pos, Vector2D size) {
        Vector2D coords = new Vector2D();
        coords.set(getCoords().getX(), getCoords().getY());

        return (coords.getX()*scaleWidth/render.getWidth() >= pos.getX()  && coords.getX()*scaleWidth/render.getWidth() <= pos.getX() + size.getX() &&
                coords.getY()*scaleHeight/render.getHeight() >= pos.getY() && coords.getY()*scaleHeight/render.getHeight() <= pos.getY() + size.getY());
    }

    public Vector2D getCoords() {
        return new Vector2D(this.touchCoords.getX(), this.touchCoords.getY());
    }

    public void setCoords(int x, int y) {
        this.touchCoords.set(x, y);
    }

    public TouchListener getTouchListener(){
        return this.touchlistener;
    }
    public LongTouchListener getLongTouchListener() { return this.longTouchListener; }
}

//Toque simple
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
        this.inputAndroid.setCoords((int)e.getX(),(int)e.getY());

        if(e.getAction() == MotionEvent.ACTION_UP){
            //Para que no se haga simple click despues de long touch
            if (cancel){
                cancel = false;
                return;
            }
            this.eventHandler.sendEvent(EventHandler.EventType.TOUCH);
        }
    }

    public void setCancel(){
        cancel = true;
    }
}

//Toque largo
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
        this.eventHandler.sendEvent(EventHandler.EventType.LONG_TOUCH);
    }
}