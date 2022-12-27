package com.example.logica;

import com.example.lib.Engine;
import com.example.lib.IAudio;
import com.example.lib.IEventHandler;
import com.example.lib.IFont;
import com.example.lib.IGraphics;
import com.example.lib.ISceneMngr;
import com.example.lib.ISound;
import com.example.lib.Input;
import com.example.lib.Scene;
import com.example.lib.Vector2D;


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
    public void update(double deltaTime){
    }

    @Override
    public void loadResources(Engine engine){

    }

    @Override
    public void render(IGraphics render){
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
    public void handleInput(IEventHandler.EventType type, IAudio audio, Input input, ISceneMngr sceneMngr){
        //5x5
        if (input.InputReceive(this.inputButton5.getPos(), this.inputButton5.getSize())){
            MyScene playScene = new MyScene(5, 5);
            sceneMngr.pushScene(playScene);
        }

        //8x8
        if (input.InputReceive(this.inputButton8.getPos(), this.inputButton8.getSize())){
            MyScene playScene = new MyScene( 8, 8);
            sceneMngr.pushScene(playScene);
        }

        //10x10
        if (input.InputReceive(this.inputButton10.getPos(), this.inputButton10.getSize())){
            MyScene playScene = new MyScene(10, 10);
            sceneMngr.pushScene(playScene);
        }

        //5x8
        if (input.InputReceive(this.inputButton5X8.getPos(), this.inputButton5X8.getSize())){
            MyScene playScene = new MyScene(5, 8);
            sceneMngr.pushScene(playScene);
        }

        //8x10
        if (input.InputReceive(this.inputButton8X10.getPos(), this.inputButton8X10.getSize())){
            MyScene playScene = new MyScene(8, 10);
            sceneMngr.pushScene(playScene);
        }

        //5x10
        if (input.InputReceive(this.inputButton5X10.getPos(), this.inputButton5X10.getSize())){
            MyScene playScene = new MyScene(5, 10);
            sceneMngr.pushScene(playScene);
        }

        //Back button
        if (input.InputReceive(this.backInputButton.getPos(), this.backInputButton.getSize())){
            sceneMngr.popScene();
        }
    }
}