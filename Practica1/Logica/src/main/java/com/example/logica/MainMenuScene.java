package com.example.logica;

import com.example.lib.Engine;
import com.example.lib.IEventHandler;
import com.example.lib.IFont;
import com.example.lib.IImage;
import com.example.lib.ISound;
import com.example.lib.Scene;
import com.example.lib.Vector2D;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.HashMap;

public class MainMenuScene implements Scene {

    private Engine engine;
    private Button play;

    public MainMenuScene(Engine engineAux) {
        this.engine = engineAux;
    }

    @Override
    public boolean inputReceived(Vector2D pos, Vector2D size) {
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), this.engine.getInput().getScaledCoords().getY());

        return (coords.getX() >= pos.getX() && coords.getX() <= pos.getX() + size.getX() &&
                coords.getY() >= pos.getY() && coords.getY() <= pos.getY() + size.getY());
    }

    @Override
    public void init() {
        try {
            //La constructora del menu solo se llama una vez
            //Cargamos el fondo y lo playeamos
            this.engine.getAudio().loadMusic("background", "assets/WiiBackgroundMusic.wav");
            this.engine.getAudio().playSound("background", 0);
            this.engine.getAudio().newSound("effect", "assets/wiiClickSound.wav");

            this.engine.getGraphics().newFont("Calibri", "assets/CalibriRegular.ttf", 0, 25);
            this.engine.getGraphics().newFont("Cooper", "assets/CooperBlackRegular.ttf", 0, 40);
            this.engine.getGraphics().newFont("CalibriSmall", "assets/CalibriRegular.ttf", 1, 18);
            this.engine.getGraphics().newFont("CooperBold", "assets/CalibriRegular.ttf", 1, 40);
            this.engine.getGraphics().newFont("CalibriBold", "assets/CalibriRegular.ttf", 1, 20);

            this.engine.getGraphics().newImage("Flecha", "assets/arrow.png");
            this.engine.getGraphics().newImage("Lupa", "assets/lupa.png");
            this.engine.getGraphics().newImage("PlayButton", "assets/playButton.png");


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        this.play = new Button(this.engine.getWidth() / 2 -(engine.getWidth()/6), this.engine.getHeight() / 2.5, engine.getWidth()/3, engine.getHeight()/4.8);
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
    public void render() {
        //Titulo
        this.engine.drawText("NONOGRAMAS", (int) (this.engine.getWidth() / 3.6), (int) (this.engine.getHeight() / 10.8), "Black", "Cooper");
        //Boton
        this.engine.drawImage((int)this.play.getPos().getX(),(int)(play.getPos().getY()) ,(int)(this.play.getSize ().getX()), (int)(this.play.getSize ().getY()), "PlayButton");

    }

    @Override
    public void handleInput() {
        //Si pulsas el boton...
        if (inputReceived(this.play.getPos(), this.play.getSize())) {
            //Te lleva a la pantalla de seleccion
            LevelSelection levelScene = new LevelSelection(this.engine);
            this.engine.setScene(levelScene);
        }
    }
}