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

        try {
            //La constructora del menu solo se llama una vez
            //Cargamos el fondo y lo playeamos
            this.engine.getAudio().loadMusic("background", "Assets\\WiiBackgroundMusic.wav");
            this.engine.getAudio().playSound("background", 0);
            this.engine.getAudio().newSound("effect", "Assets\\wiiClickSound.wav");

            this.engine.getGraphics().newFont("Calibri", "Assets\\CalibriRegular.ttf", 0, 25);
            this.engine.getGraphics().newFont("Cooper", "Assets\\CooperBlackRegular.ttf", 0, 40);
            this.engine.getGraphics().newFont("CalibriSmall", "Assets\\CalibriRegular.ttf", 1, 18);
            this.engine.getGraphics().newFont("CooperBold", "Assets\\CalibriRegular.ttf", 1, 40);
            this.engine.getGraphics().newFont("CalibriBold", "Assets\\CalibriRegular.ttf", 1, 20);

            this.engine.getGraphics().newImage("Flecha", "Assets\\arrow.png");
            this.engine.getGraphics().newImage("Lupa", "Assets\\lupa.png");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        this.play = new Button(this.engine.getWidth() / 2.4, this.engine.getHeight() / 2.5, 70, 40);
    }

    @Override
    public boolean inputReceived(Vector2D pos, Vector2D size) {
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), this.engine.getInput().getScaledCoords().getY());

        return (coords.getX() >= pos.getX() && coords.getX() <= pos.getX() + size.getX() &&
                coords.getY() >= pos.getY() && coords.getY() <= pos.getY() + size.getY());
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
        this.engine.drawText("Jugar", (int) (this.play.getPos().getX() + this.play.getSize().getX() / 5), (int) (this.play.getPos().getY() + this.play.getSize().getY() / 2), "Black", "Calibri");
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