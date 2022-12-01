package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

public class LevelSelection implements Scene {

    private Button button5;
    private Button button8;
    private Button button10;
    private Button button5x8;
    private Button button8x10;
    private Button button5x10;

    private Button backButton;

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
        this.button5 = new Button(engine.getWidth()/4  - engine.getWidth()/12, engine.getHeight()/3.6, engine.getWidth()/6, engine.getHeight()/9);
        this.button8 = new Button(engine.getWidth()/2 - engine.getWidth()/12, engine.getHeight()/3.6, engine.getWidth()/6, engine.getHeight()/9);
        this.button10 = new Button(engine.getWidth()*3/4  - engine.getWidth()/12, engine.getHeight()/3.6, engine.getWidth()/6, engine.getHeight()/9);
        this.button5x8 = new Button(engine.getWidth()/4  - engine.getWidth()/12, engine.getHeight()/2.2, engine.getWidth()/6, engine.getHeight()/9);
        this.button8x10 = new Button(engine.getWidth()/2 - engine.getWidth()/12, engine.getHeight()/2.2, engine.getWidth()/6, engine.getHeight()/9);
        this.button5x10 = new Button(engine.getWidth()*3/4  - engine.getWidth()/12, engine.getHeight()/2.2, engine.getWidth()/6, engine.getHeight()/9);
        this.backButton = new Button(engine.getWidth()/72 + engine.getWidth()/44, engine.getHeight()/22, engine.getWidth()/10, engine.getHeight()/15);
    }

    @Override
    public void update(double deltaTime){
        //Para los eventos...
        if(engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput(engine.getEventMngr().getEventType());
            engine.getEventMngr().sendEvent(EventHandler.EventType.NONE);
        }
    }

    @Override
    public void render(){
        //5x5
        this.engine.drawCircle(button5.getPos().getX(), button5.getPos().getY(), button5.getSize().getX()/2, "purple");
        this.engine.drawText("5x5", (int)(button5.getPos().getX() + button5.getSize().getX()/2), (int)(button5.getPos().getY() + button5.getSize().getY()/1.8), "Black","Amor", 0);
        //8x8
        this.engine.drawCircle(button8.getPos().getX(), button8.getPos().getY(), button8.getSize().getX()/2, "purple");
        this.engine.drawText("8x8", (int)(button8.getPos().getX() + button8.getSize().getX()/2), (int)(button8.getPos().getY() + button8.getSize().getY()/1.8), "Black","Amor", 0);
        //10x10
        this.engine.drawCircle(button10.getPos().getX(), button10.getPos().getY(), button10.getSize().getX()/2, "purple");
        this.engine.drawText("10x10", (int)(button10.getPos().getX() + button10.getSize().getX()/2), (int)(button10.getPos().getY() + button10.getSize().getY()/1.8), "Black", "Amor", 0);
        //5x8
        this.engine.drawCircle(button5x8.getPos().getX(), button5x8.getPos().getY(), button5x8.getSize().getX()/2, "purple");
        this.engine.drawText("5x8", (int)(button5x8.getPos().getX() + button5x8.getSize().getX()/2), (int)(button5x8.getPos().getY() + button5x8.getSize().getY()/1.8), "Black", "Amor", 0);
        //8x10
        this.engine.drawCircle(button8x10.getPos().getX(), button8x10.getPos().getY(), button8x10.getSize().getX()/2, "purple");
        this.engine.drawText("8x10", (int)(button8x10.getPos().getX() + button8x10.getSize().getX()/2), (int)(button8x10.getPos().getY() + button8x10.getSize().getY()/1.8), "Black", "Amor", 0);
        //5x10
        this.engine.drawCircle(button5x10.getPos().getX(), button5x10.getPos().getY(), button5x10.getSize().getX()/2, "purple");
        this.engine.drawText("5x10", (int)(button5x10.getPos().getX() + button5x10.getSize().getX()/2), (int)(button5x10.getPos().getY() + button5x10.getSize().getY()/1.8), "Black", "Amor", 0);

        //Back Button
        this.engine.drawImage((int)backButton.getPos().getX(),(int)backButton.getPos().getY(),(int)(backButton.getSize().getX()),(int)(backButton.getSize().getY()), "Back");

        //Texto indicativo
        this.engine.drawText("Selecciona el tamaño del puzzle", (int)(engine.getWidth()/2), (int)(engine.getHeight()/5.4), "Black", "Amor", 0);
    }

    @Override
    public void handleInput(EventHandler.EventType type){
        //5x5
        if (inputReceived(this.button5.getPos(), this.button5.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 5, 5);
            this.engine.setScene(playScene);
        }

        //8x8
        if (inputReceived(this.button8.getPos(), this.button8.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 8, 8);
            this.engine.setScene(playScene);
        }

        //10x10
        if (inputReceived(this.button10.getPos(), this.button10.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 10, 10);
            this.engine.setScene(playScene);
        }

        //5x8
        if (inputReceived(this.button5x8.getPos(), this.button5x8.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 5, 8);
            this.engine.setScene(playScene);
        }

        //8x10
        if (inputReceived(this.button8x10.getPos(), this.button8x10.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 8, 10);
            this.engine.setScene(playScene);
        }

        //5x10
        if (inputReceived(this.button5x10.getPos(), this.button5x10.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 5, 10);
            this.engine.setScene(playScene);
        }

        //Back button
        if (inputReceived(this.backButton.getPos(), this.backButton.getSize())){
            this.engine.popScene();
        }
    }
}
