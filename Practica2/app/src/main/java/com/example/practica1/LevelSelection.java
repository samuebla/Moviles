package com.example.practica1;

import com.example.engineandroid.AdManager;
import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.RenderAndroid;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.io.Serializable;

public class LevelSelection implements Scene {

    int scaleHeight;
    int scaleWidth;
    private InputButton inputButton5;
    private InputButton inputButton8;
    private InputButton inputButton10;
    private InputButton inputButton5X8;
    private InputButton inputButton8X10;
    private InputButton inputButton5X10;

    private InputButton backInputButton;

    private EngineApp engine;

    public LevelSelection(EngineApp engineAux){

        this.engine = engineAux;
        scaleHeight = 1000;
        scaleWidth = 1000;
        init();
    }

    @Override
    public boolean inputReceived(Vector2D pos, Vector2D size){
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), engine.getInput().getScaledCoords().getY());

        return (coords.getX() * scaleWidth / engine.getGraphics().getWidth() >= pos.getX() && coords.getX() * scaleWidth / engine.getGraphics().getWidth() <= pos.getX() + size.getX() &&
                coords.getY() * scaleHeight / engine.getGraphics().getHeight() >= pos.getY() && coords.getY() * scaleHeight / engine.getGraphics().getHeight() <= pos.getY() + size.getY());
    }

    @Override
    public void init() {
        //Botones selectores del nivel
        this.inputButton5 = new InputButton(scaleWidth/4  - scaleWidth/12, scaleHeight/3.6, scaleWidth/6, scaleHeight/9);
        this.inputButton8 = new InputButton(scaleWidth/2 - scaleWidth/12,scaleHeight/3.6, scaleWidth/6, scaleHeight/9);
        this.inputButton10 = new InputButton(scaleWidth*3/4  - scaleWidth/12, scaleHeight/3.6, scaleWidth/6, scaleHeight/9);
        this.inputButton5X8 = new InputButton(scaleWidth/4  - scaleWidth/12, scaleHeight/2.2, scaleWidth/6, scaleHeight/9);
        this.inputButton8X10 = new InputButton(scaleWidth/2 - scaleWidth/12, scaleHeight/2.2, scaleWidth/6, scaleHeight/9);
        this.inputButton5X10 = new InputButton(scaleWidth*3/4  -scaleWidth/12, scaleHeight/2.2, scaleWidth/6, scaleHeight/9);
        this.backInputButton = new InputButton(scaleWidth/72 + scaleWidth/44, scaleHeight/22, scaleWidth/10, scaleHeight/15);
    }

    @Override
    public void loadResources(EngineApp engineAux) {

    }

    @Override
    public void update(double deltaTime, AdManager adManager){
        //Para los eventos...
        if(engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput(engine.getEventMngr().getEventType(), adManager);
            engine.getEventMngr().sendEvent(EventHandler.EventType.NONE);
        }
    }

    @Override
    public void render(RenderAndroid render){
        //5x5
        render.drawCircle(inputButton5.getPos().getX(), inputButton5.getPos().getY(), inputButton5.getSize().getX()/2, "purple");
        render.drawText("5x5", (int)(inputButton5.getPos().getX() + inputButton5.getSize().getX()/2), (int)(inputButton5.getPos().getY() + inputButton5.getSize().getY()/1.5), "Black","Amor", 0);
        //8x8
        render.drawCircle(inputButton8.getPos().getX(), inputButton8.getPos().getY(), inputButton8.getSize().getX()/2, "purple");
        render.drawText("8x8", (int)(inputButton8.getPos().getX() + inputButton8.getSize().getX()/2), (int)(inputButton8.getPos().getY() + inputButton8.getSize().getY()/1.5), "Black","Amor", 0);
        //10x10
        render.drawCircle(inputButton10.getPos().getX(), inputButton10.getPos().getY(), inputButton10.getSize().getX()/2, "purple");
        render.drawText("10x10", (int)(inputButton10.getPos().getX() + inputButton10.getSize().getX()/2), (int)(inputButton10.getPos().getY() + inputButton10.getSize().getY()/1.5), "Black", "Amor", 0);
        //5x8
        render.drawCircle(inputButton5X8.getPos().getX(), inputButton5X8.getPos().getY(), inputButton5X8.getSize().getX()/2, "purple");
        render.drawText("5x8", (int)(inputButton5X8.getPos().getX() + inputButton5X8.getSize().getX()/2), (int)(inputButton5X8.getPos().getY() + inputButton5X8.getSize().getY()/1.5), "Black", "Amor", 0);
        //8x10
        render.drawCircle(inputButton8X10.getPos().getX(), inputButton8X10.getPos().getY(), inputButton8X10.getSize().getX()/2, "purple");
        render.drawText("8x10", (int)(inputButton8X10.getPos().getX() + inputButton8X10.getSize().getX()/2), (int)(inputButton8X10.getPos().getY() + inputButton8X10.getSize().getY()/1.5), "Black", "Amor", 0);
        //5x10
        render.drawCircle(inputButton5X10.getPos().getX(), inputButton5X10.getPos().getY(), inputButton5X10.getSize().getX()/2, "purple");
        render.drawText("5x10", (int)(inputButton5X10.getPos().getX() + inputButton5X10.getSize().getX()/2), (int)(inputButton5X10.getPos().getY() + inputButton5X10.getSize().getY()/1.5), "Black", "Amor", 0);

        //Back Button
        render.drawImage((int) backInputButton.getPos().getX(),(int) backInputButton.getPos().getY(),(int)(backInputButton.getSize().getX()),(int)(backInputButton.getSize().getY()), "Back");

        //Texto indicativo
        render.drawText("Selecciona el tamaño del puzzle", (int)(scaleWidth/2), (int)(scaleHeight/5.4), "Black", "Amor", 0);
    }

    @Override
    public void handleInput(EventHandler.EventType type, AdManager adManager){
        //5x5
        if (inputReceived(this.inputButton5.getPos(), this.inputButton5.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 5, 5);
            this.engine.getSceneMngr().pushScene(playScene);
        }

        //8x8
        if (inputReceived(this.inputButton8.getPos(), this.inputButton8.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 8, 8);
            this.engine.getSceneMngr().pushScene(playScene);
        }

        //10x10
        if (inputReceived(this.inputButton10.getPos(), this.inputButton10.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 10, 10);
            this.engine.getSceneMngr().pushScene(playScene);
        }

        //5x8
        if (inputReceived(this.inputButton5X8.getPos(), this.inputButton5X8.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 5, 8);
            this.engine.getSceneMngr().pushScene(playScene);
        }

        //8x10
        if (inputReceived(this.inputButton8X10.getPos(), this.inputButton8X10.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 8, 10);
            this.engine.getSceneMngr().pushScene(playScene);
        }

        //5x10
        if (inputReceived(this.inputButton5X10.getPos(), this.inputButton5X10.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 5, 10);
            this.engine.getSceneMngr().pushScene(playScene);
        }

        //Back button
        if (inputReceived(this.backInputButton.getPos(), this.backInputButton.getSize())){
            this.engine.getSceneMngr().popScene();
        }
    }

    //Se llama cuando la escena posterior se elimina y se vuelve aqui, por si hay que actualizar algo
    @Override
    public void onResume() {
        init();
    }
}
