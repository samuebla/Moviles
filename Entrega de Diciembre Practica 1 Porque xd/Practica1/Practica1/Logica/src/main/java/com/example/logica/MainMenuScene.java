package com.example.logica;

import com.example.lib.Engine;
import com.example.lib.IEventHandler;
import com.example.lib.IFont;
import com.example.lib.IGraphics;
import com.example.lib.IImage;
import com.example.lib.ISceneMngr;
import com.example.lib.ISound;
import com.example.lib.Input;
import com.example.lib.Scene;
import com.example.lib.Vector2D;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.HashMap;

public class MainMenuScene implements Scene {

    private Button play;

    public MainMenuScene() {
        init();
    }

    @Override
    public void loadResources(Engine engine){
        try {
            //La constructora del menu solo se llama una vez
            //Cargamos el fondo y lo playeamos
            engine.getAudio().loadMusic("background", "assets/WiiBackgroundMusic.wav");
            engine.getAudio().playSound("background", 0);
            engine.getAudio().newSound("effect", "assets/wiiClickSound.wav");

            engine.getGraphics().newFont("Amor", "assets/AmorRegular.ttf", 0, 50);
            engine.getGraphics().newFont("Calibri", "assets/CalibriRegular.ttf", 0, 25);
            engine.getGraphics().newFont("Cooper", "assets/CooperBlackRegular.ttf", 0, 50);
            engine.getGraphics().newFont("CalibriSmall", "assets/CalibriRegular.ttf", 0, 22);
            engine.getGraphics().newFont("CooperBold", "assets/CalibriRegular.ttf", 1, 40);
            engine.getGraphics().newFont("CalibriBold", "assets/CalibriRegular.ttf", 1, 20);

            engine.getGraphics().newImage("Board", "assets/board.png");
            engine.getGraphics().newImage("GiveUp", "assets/giveUpButton.png");
            engine.getGraphics().newImage("Back", "assets/backButton.png");
            engine.getGraphics().newImage("Check", "assets/checkButton.png");

            engine.getGraphics().newImage("PlayButton", "assets/playButton.png");

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void init() {
        this.play = new Button(this.engine.getWidth() / 2 - (engine.getWidth()/6), this.engine.getHeight() / 2.5, engine.getWidth()/3, engine.getHeight()/4.8);
        System.out.println("Boton[X] " + this.play.pos.getX() +" [Y] " + this.play.pos.getY());
    }

    @Override
    public void update(double deltaTime) {
        //Para los eventos
        if (this.engine.getEventMngr().getEvent().eventType != IEventHandler.EventType.NONE) {
            handleInput();
            this.engine.getEventMngr().sendEvent(IEventHandler.EventType.NONE);
        }
    }

    @Override
    public void render(IGraphics render) {
        //Titulo
        this.engine.drawText("NONOGRAMAS", (int) (this.engine.getWidth() / 2), (int) (this.engine.getHeight() / 10.8), "Black", "Cooper", 0);
        //Boton
        this.engine.drawImage((int)this.play.getPos().getX(),(int)(play.getPos().getY()) ,(int)(this.play.getSize ().getX()), (int)(this.play.getSize ().getY()), "PlayButton");

    }

    @Override
    public void handleInput(IEventHandler.EventType type, ISound sound, Input input, ISceneMngr sceneMngr) {
        //Si pulsas el boton...
        if (inputReceived(this.play.getPos(), this.play.getSize())) {
            //Te lleva a la pantalla de seleccion
            LevelSelection levelScene = new LevelSelection(this.engine);
            this.engine.setScene(levelScene);
        }
    }
}