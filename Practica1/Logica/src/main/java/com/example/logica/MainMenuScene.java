package com.example.logica;

import com.example.lib.Engine;
import com.example.lib.IEventHandler;
import com.example.lib.IFont;
import com.example.lib.IImage;
import com.example.lib.ISound;
import com.example.lib.Scene;
import com.example.lib.Vector2D;

import java.awt.geom.Point2D;
import java.util.HashMap;

public class MainMenuScene implements Scene {

    private Engine engine;

    HashMap<String, IFont> fonts;
    HashMap<String, IImage> images;
    HashMap<String, ISound> sounds;


    private Button play;

    public MainMenuScene(Engine engineAux, IFont[] fontsAux, String[] keys, IImage[] imagesAux, String[] imageKeys,String[] soundKeys,ISound[] soundsAux){
        this.engine = engineAux;

        this.fonts = new HashMap<>();

        this.sounds = new HashMap<>();

        //Metemos laos sonidos
        for (int i = 0; i < soundsAux.length; i++) {
            this.sounds.put(soundKeys[i], soundsAux[i]);
        }

        //Metemos las fuentes
        for (int i = 0; i < fontsAux.length; i++) {
            this.fonts.put(keys[i], fontsAux[i]);
        }

        this.images = new HashMap<>();

        //Metemos las fuentes
        for (int i = 0; i < imagesAux.length; i++) {
            this.images.put(imageKeys[i], imagesAux[i]);
        }

        //Playeamos la musica
        sounds.get("background").play();
        //Lo loopeamos para que suene siempre
        sounds.get("background").startLoop();


        play = new Button(engine.getWidth()/2.4, engine.getHeight()/2.5, 70, 40);
    }

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
        engine.drawText("NONOGRAMAS", (int)(engine.getWidth()/3.6), (int)(engine.getHeight()/10.8), "Black", fonts.get("Cooper"));
        engine.drawText("Jugar", (int)(play.getPos().getX() + play.getSize().getX()/5), (int)(play.getPos().getY() + play.getSize().getY()/2), "Black", fonts.get("Calibri"));
    }

    @Override
    public void handleInput(){
        if (inputReceived(play.getPos(), play.getSize())){
            LevelSelection levelScene = new LevelSelection(this.engine, this.fonts, this.images,this.sounds);
            this.engine.setScene(levelScene);
        }
    }
}
