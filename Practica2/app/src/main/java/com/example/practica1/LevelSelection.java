package com.example.practica1;

import com.example.engineandroid.AdManager;
import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.io.Serializable;

public class LevelSelection implements Scene, Serializable {

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

        init();
    }

    @Override
    public boolean inputReceived(Vector2D pos, Vector2D size){
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), engine.getInput().getScaledCoords().getY());

        return (coords.getX() >= pos.getX() && coords.getX() <= pos.getX() + size.getX() &&
                coords.getY() >= pos.getY() && coords.getY() <= pos.getY() + size.getY());
    }

    @Override
    public void init() {
        //Botones selectores del nivel
        this.inputButton5 = new InputButton(engine.getGraphics().getWidth()/4  - engine.getGraphics().getWidth()/12, engine.getGraphics().getHeight()/3.6, engine.getGraphics().getWidth()/6, engine.getGraphics().getHeight()/9);
        this.inputButton8 = new InputButton(engine.getGraphics().getWidth()/2 - engine.getGraphics().getWidth()/12, engine.getGraphics().getHeight()/3.6, engine.getGraphics().getWidth()/6, engine.getGraphics().getHeight()/9);
        this.inputButton10 = new InputButton(engine.getGraphics().getWidth()*3/4  - engine.getGraphics().getWidth()/12, engine.getGraphics().getHeight()/3.6, engine.getGraphics().getWidth()/6, engine.getGraphics().getHeight()/9);
        this.inputButton5X8 = new InputButton(engine.getGraphics().getWidth()/4  - engine.getGraphics().getWidth()/12, engine.getGraphics().getHeight()/2.2, engine.getGraphics().getWidth()/6, engine.getGraphics().getHeight()/9);
        this.inputButton8X10 = new InputButton(engine.getGraphics().getWidth()/2 - engine.getGraphics().getWidth()/12, engine.getGraphics().getHeight()/2.2, engine.getGraphics().getWidth()/6, engine.getGraphics().getHeight()/9);
        this.inputButton5X10 = new InputButton(engine.getGraphics().getWidth()*3/4  - engine.getGraphics().getWidth()/12, engine.getGraphics().getHeight()/2.2, engine.getGraphics().getWidth()/6, engine.getGraphics().getHeight()/9);
        this.backInputButton = new InputButton(engine.getGraphics().getWidth()/72 + engine.getGraphics().getWidth()/44, engine.getGraphics().getHeight()/22, engine.getGraphics().getWidth()/10, engine.getGraphics().getHeight()/15);
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
    public void render(){
        //5x5
        this.engine.getGraphics().drawCircle(inputButton5.getPos().getX(), inputButton5.getPos().getY(), inputButton5.getSize().getX()/2, "purple");
        this.engine.getGraphics().drawText("5x5", (int)(inputButton5.getPos().getX() + inputButton5.getSize().getX()/2), (int)(inputButton5.getPos().getY() + inputButton5.getSize().getY()/1.8), "Black","Amor", 0);
        //8x8
        this.engine.getGraphics().drawCircle(inputButton8.getPos().getX(), inputButton8.getPos().getY(), inputButton8.getSize().getX()/2, "purple");
        this.engine.getGraphics().drawText("8x8", (int)(inputButton8.getPos().getX() + inputButton8.getSize().getX()/2), (int)(inputButton8.getPos().getY() + inputButton8.getSize().getY()/1.8), "Black","Amor", 0);
        //10x10
        this.engine.getGraphics().drawCircle(inputButton10.getPos().getX(), inputButton10.getPos().getY(), inputButton10.getSize().getX()/2, "purple");
        this.engine.getGraphics().drawText("10x10", (int)(inputButton10.getPos().getX() + inputButton10.getSize().getX()/2), (int)(inputButton10.getPos().getY() + inputButton10.getSize().getY()/1.8), "Black", "Amor", 0);
        //5x8
        this.engine.getGraphics().drawCircle(inputButton5X8.getPos().getX(), inputButton5X8.getPos().getY(), inputButton5X8.getSize().getX()/2, "purple");
        this.engine.getGraphics().drawText("5x8", (int)(inputButton5X8.getPos().getX() + inputButton5X8.getSize().getX()/2), (int)(inputButton5X8.getPos().getY() + inputButton5X8.getSize().getY()/1.8), "Black", "Amor", 0);
        //8x10
        this.engine.getGraphics().drawCircle(inputButton8X10.getPos().getX(), inputButton8X10.getPos().getY(), inputButton8X10.getSize().getX()/2, "purple");
        this.engine.getGraphics().drawText("8x10", (int)(inputButton8X10.getPos().getX() + inputButton8X10.getSize().getX()/2), (int)(inputButton8X10.getPos().getY() + inputButton8X10.getSize().getY()/1.8), "Black", "Amor", 0);
        //5x10
        this.engine.getGraphics().drawCircle(inputButton5X10.getPos().getX(), inputButton5X10.getPos().getY(), inputButton5X10.getSize().getX()/2, "purple");
        this.engine.getGraphics().drawText("5x10", (int)(inputButton5X10.getPos().getX() + inputButton5X10.getSize().getX()/2), (int)(inputButton5X10.getPos().getY() + inputButton5X10.getSize().getY()/1.8), "Black", "Amor", 0);

        //Back Button
        this.engine.getGraphics().drawImage((int) backInputButton.getPos().getX(),(int) backInputButton.getPos().getY(),(int)(backInputButton.getSize().getX()),(int)(backInputButton.getSize().getY()), "Back");

        //Texto indicativo
        this.engine.getGraphics().drawText("Selecciona el tamaño del puzzle", (int)(engine.getGraphics().getWidth()/2), (int)(engine.getGraphics().getHeight()/5.4), "Black", "Amor", 0);
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
