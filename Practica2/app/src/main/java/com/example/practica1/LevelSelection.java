package com.example.practica1;

import com.example.engineandroid.AdManager;
import com.example.engineandroid.AudioAndroid;
import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.InputAndroid;
import com.example.engineandroid.RenderAndroid;
import com.example.engineandroid.Scene;
import com.example.engineandroid.SceneMngrAndroid;
import com.example.engineandroid.Vector2D;

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

    public LevelSelection(){
        scaleHeight = 1000;
        scaleWidth = 1000;
        init();
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
    public void onStop() {

    }

    @Override
    public void loadResources(EngineApp engineAux) {

    }

    @Override
    public void update(double deltaTime){
    }

    @Override
    public void render(RenderAndroid render){
        //5x5
        render.drawCircle(inputButton5.getPos().getX(), inputButton5.getPos().getY(), inputButton5.getSize().getX()/2, "purple");
        render.drawText("5x5", (int)(inputButton5.getPos().getX() + inputButton5.getSize().getX()/2), (int)(inputButton5.getPos().getY() + inputButton5.getSize().getY()/1.5), "Black","Amor", 0,scaleWidth/15);
        //8x8
        render.drawCircle(inputButton8.getPos().getX(), inputButton8.getPos().getY(), inputButton8.getSize().getX()/2, "purple");
        render.drawText("8x8", (int)(inputButton8.getPos().getX() + inputButton8.getSize().getX()/2), (int)(inputButton8.getPos().getY() + inputButton8.getSize().getY()/1.5), "Black","Amor", 0,scaleWidth/15);
        //10x10
        render.drawCircle(inputButton10.getPos().getX(), inputButton10.getPos().getY(), inputButton10.getSize().getX()/2, "purple");
        render.drawText("10x10", (int)(inputButton10.getPos().getX() + inputButton10.getSize().getX()/2), (int)(inputButton10.getPos().getY() + inputButton10.getSize().getY()/1.5), "Black", "Amor", 0,scaleWidth/15);
        //5x8
        render.drawCircle(inputButton5X8.getPos().getX(), inputButton5X8.getPos().getY(), inputButton5X8.getSize().getX()/2, "purple");
        render.drawText("5x8", (int)(inputButton5X8.getPos().getX() + inputButton5X8.getSize().getX()/2), (int)(inputButton5X8.getPos().getY() + inputButton5X8.getSize().getY()/1.5), "Black", "Amor", 0,scaleWidth/15);
        //8x10
        render.drawCircle(inputButton8X10.getPos().getX(), inputButton8X10.getPos().getY(), inputButton8X10.getSize().getX()/2, "purple");
        render.drawText("8x10", (int)(inputButton8X10.getPos().getX() + inputButton8X10.getSize().getX()/2), (int)(inputButton8X10.getPos().getY() + inputButton8X10.getSize().getY()/1.5), "Black", "Amor", 0,scaleWidth/15);
        //5x10
        render.drawCircle(inputButton5X10.getPos().getX(), inputButton5X10.getPos().getY(), inputButton5X10.getSize().getX()/2, "purple");
        render.drawText("5x10", (int)(inputButton5X10.getPos().getX() + inputButton5X10.getSize().getX()/2), (int)(inputButton5X10.getPos().getY() + inputButton5X10.getSize().getY()/1.5), "Black", "Amor", 0,scaleWidth/15);

        //Back Button
        render.drawImage((int) backInputButton.getPos().getX(),(int) backInputButton.getPos().getY(),(int)(backInputButton.getSize().getX()),(int)(backInputButton.getSize().getY()), "Back");

        //Texto indicativo
        render.drawText("Selecciona el tamaño del puzzle", (int)(scaleWidth/2.0), (int)(scaleHeight/5.4), "Black", "Amor", 0,scaleWidth/15);
    }

    @Override
    public void handleInput(EventHandler.EventType type, AdManager adManager, InputAndroid input, SceneMngrAndroid sceneMngr, AudioAndroid audio, RenderAndroid render){
        //5x5
        if (input.inputReceived(this.inputButton5.getPos(), this.inputButton5.getSize())){
            QuickGameScene playScene = new QuickGameScene(5, 5);
            sceneMngr.pushScene(playScene);
        }

        //8x8
        if (input.inputReceived(this.inputButton8.getPos(), this.inputButton8.getSize())){
            QuickGameScene playScene = new QuickGameScene( 8, 8);
            sceneMngr.pushScene(playScene);
        }

        //10x10
        if (input.inputReceived(this.inputButton10.getPos(), this.inputButton10.getSize())){
            QuickGameScene playScene = new QuickGameScene(10, 10);
            sceneMngr.pushScene(playScene);
        }

        //5x8
        if (input.inputReceived(this.inputButton5X8.getPos(), this.inputButton5X8.getSize())){
            QuickGameScene playScene = new QuickGameScene(5, 8);
            sceneMngr.pushScene(playScene);
        }

        //8x10
        if (input.inputReceived(this.inputButton8X10.getPos(), this.inputButton8X10.getSize())){
            QuickGameScene playScene = new QuickGameScene(8, 10);
            sceneMngr.pushScene(playScene);
        }

        //5x10
        if (input.inputReceived(this.inputButton5X10.getPos(), this.inputButton5X10.getSize())){
            QuickGameScene playScene = new QuickGameScene(5, 10);
            sceneMngr.pushScene(playScene);
        }

        //Back button
        if (input.inputReceived(this.backInputButton.getPos(), this.backInputButton.getSize())){
            sceneMngr.popScene();
        }
    }
}
