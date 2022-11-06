package com.example.logica;

import com.example.lib.Engine;
import com.example.lib.IEventHandler;
import com.example.lib.IFont;
import com.example.lib.Scene;

import java.awt.geom.Point2D;
import java.util.HashMap;

public class MainMenuScene implements Scene {

    private Engine engine;

    HashMap<String, IFont> fonts;

    private Button play;

    public MainMenuScene(Engine engineAux, IFont[] fontsAux, String[] keys){
        this.engine = engineAux;

        fonts = new HashMap<>();

        //Metemos las fuentes
        for (int i = 0; i < fontsAux.length; i++) {
            fonts.put(keys[i], fontsAux[i]);
        }

        play = new Button(300, 300, 70, 40);
    }

    public boolean inputReceived(Point2D pos, Point2D size){
        Point2D coords = new Point2D.Double();
        coords.setLocation(engine.getInput().getRawCoords().getX(), engine.getInput().getRawCoords().getY());

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
        engine.drawText("NONOGRAMAS", 200, 100, "Black", fonts.get("Cooper"));
        engine.drawText("Jugar", (int)(play.getPos().getX() + play.getSize().getX()/5), (int)(play.getPos().getY() + play.getSize().getY()/2), "Black", fonts.get("Calibri"));
    }

    @Override
    public void handleInput(){
        if (inputReceived(play.getPos(), play.getSize())){
            LevelSelection levelScene = new LevelSelection(this.engine, this.fonts);
            this.engine.setScene(levelScene);
        }
    }
}
