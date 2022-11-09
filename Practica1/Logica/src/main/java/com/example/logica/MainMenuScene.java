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

    public MainMenuScene(Engine engineAux){
        this.engine = engineAux;

        //La constructora del menu solo se llama una vez
        //Cargamos el fondo y lo playeamos
        engine.getAudio().newSound("background","Assets\\WiiBackgroundMusic.wav").play();
        engine.getAudio().setLoop("background");
        engine.getAudio().newSound("effect","Assets\\wiiClickSound.wav");

        engine.getGraphics().newFont("Calibri","Assets\\CalibriRegular.ttf", 0, 25);
        engine.getGraphics().newFont("Cooper","Assets\\CooperBlackRegular.ttf", 0, 40);
        engine.getGraphics().newFont("CalibriSmall","Assets\\CalibriRegular.ttf", 1, 18);
        engine.getGraphics().newFont("CooperBold","Assets\\CalibriRegular.ttf", 1, 40);
        engine.getGraphics().newFont("CalibriBold","Assets\\CalibriRegular.ttf", 1, 20);

        engine.getGraphics().newImage("Flecha","Assets\\arrow.png");
        engine.getGraphics().newImage("Lupa","Assets\\lupa.png");

        play = new Button(engine.getWidth()/2.4, engine.getHeight()/2.5, 70, 40);
    }

    @Override
    public boolean inputReceived(Vector2D pos, Vector2D size){
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), engine.getInput().getScaledCoords().getY());

        return (coords.getX() >= pos.getX() && coords.getX() <= pos.getX() + size.getX() &&
                coords.getY() >= pos.getY() && coords.getY() <= pos.getY() + size.getY());
    }

    @Override
    public void update(double deltaTime){
        if(engine.getEventMngr().getEvent().eventType != IEventHandler.EventType.NONE) {
            handleInput();
            engine.getEventMngr().sendEvent(IEventHandler.EventType.NONE);
        }
    }

    @Override
    public void render(){
        engine.drawText("NONOGRAMAS", (int)(engine.getWidth()/3.6), (int)(engine.getHeight()/10.8), "Black", "Cooper");
        engine.drawText("Jugar", (int)(play.getPos().getX() + play.getSize().getX()/5), (int)(play.getPos().getY() + play.getSize().getY()/2), "Black", "Calibri");
    }

    @Override
    public void handleInput(){
        if (inputReceived(play.getPos(), play.getSize())){
            LevelSelection levelScene = new LevelSelection(this.engine);
            this.engine.setScene(levelScene);
        }
    }
}