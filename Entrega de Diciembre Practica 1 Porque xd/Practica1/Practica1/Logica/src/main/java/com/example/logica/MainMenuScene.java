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
    //Escala logica de la pantalla, de 0 a 1000, independiente del verdadero ancho y alto del surface
    int scaleWidth = 1000;
    int scaleHeight = 1000;

    private InputButton play;

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

            engine.getGraphics().newFont("Amor", "assets/AmorRegular.ttf", 0);
            engine.getGraphics().newFont("Calibri", "assets/CalibriRegular.ttf", 0);
            engine.getGraphics().newFont("Cooper", "assets/CooperBlackRegular.ttf", 0);
            engine.getGraphics().newFont("CooperBold", "assets/CalibriRegular.ttf", 1);
            engine.getGraphics().newFont("CalibriBold", "assets/CalibriRegular.ttf", 1);

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
        this.play = new InputButton(scaleWidth / 2 - (scaleWidth / 4), scaleHeight / 5, scaleWidth / 2, scaleHeight / 4.8);
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(IGraphics render) {
        //Titulo
        render.drawText("NONOGRAMAS", (int) (scaleWidth / 2.0), (int) (scaleHeight / 10.8), "Black", "Cooper", 0, (scaleWidth / 14));

        //Botones
        render.drawImage((int) this.play.getPos().getX(), (int) (play.getPos().getY()), (int) (this.play.getSize().getX()), (int) (this.play.getSize().getY()), "QuickPlay");

    }

    @Override
    public void handleInput(IEventHandler.EventType type, ISound sound, Input input, ISceneMngr sceneMngr) {
        //Si pulsas el boton...
        if (input.inputReceived(this.play.getPos(), this.play.getSize())) {
            //Te lleva a la pantalla de seleccion
            LevelSelection levelScene = new LevelSelection();
            sceneMngr.pushScene(levelScene);
        }
    }
}