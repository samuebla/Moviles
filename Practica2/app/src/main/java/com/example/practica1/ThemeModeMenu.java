package com.example.practica1;

import android.content.Context;

import com.example.engineandroid.AdManager;
import com.example.engineandroid.AudioAndroid;
import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.InputAndroid;
import com.example.engineandroid.RenderAndroid;
import com.example.engineandroid.Scene;
import com.example.engineandroid.SceneMngrAndroid;

import java.util.concurrent.atomic.AtomicReference;

public class ThemeModeMenu implements Scene {
    int scaleWidth, scaleHeight;

    private InputButton alfabetoInputButtonMode;
    private InputButton geometriaInputButtonMode;
    private InputButton fiestaInputButtonMode;
    private InputButton animalesInputButtonMode;

    private InputButton backInputButton;

    private final AtomicReference<Integer> coins;
    private Integer coinSize;

    private final AtomicReference<Integer>[] progress;
    private final AtomicReference<Integer>[] palettes;

    Context context;

    public ThemeModeMenu(Context context, AtomicReference<Integer> coinsAux, AtomicReference<Integer>[] progressAux, AtomicReference<Integer>[] palettesAux){
        this.context = context;
        this.coins = coinsAux;
        this.progress = progressAux;
        this.palettes = palettesAux;

        //Por defecto la escala es 1000x1000 pero podemos cambiarlo (recordar llamar al engine para cambiarlo)
        scaleHeight=1000;
        scaleWidth=1000;

        init();
    }

    @Override
    public void init() {
        coinSize = scaleWidth/10;
        //Botones selectores del nivel
        this.alfabetoInputButtonMode = new InputButton(scaleWidth/4  - scaleWidth/8, scaleHeight/2.4, scaleWidth/4, scaleHeight/6);
        this.geometriaInputButtonMode = new InputButton(scaleWidth*3/4 - scaleWidth/8, scaleHeight/1.5, scaleWidth/4, scaleHeight/6);
        this.animalesInputButtonMode = new InputButton(scaleWidth/4 - scaleWidth/8, scaleHeight/1.5, scaleWidth/4, scaleHeight/6);
        this.fiestaInputButtonMode = new InputButton(scaleWidth*3/4 - scaleWidth/8, scaleHeight/2.4, scaleWidth/4, scaleHeight/6);



        this.backInputButton = new InputButton(10,10, scaleWidth/10, scaleHeight/15);
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
        //ALFABETO
        render.drawImage((int)this.alfabetoInputButtonMode.getPos().getX(), (int)this.alfabetoInputButtonMode.getPos().getY(),(int)this.alfabetoInputButtonMode.getSize().getX(),(int)this.alfabetoInputButtonMode.getSize().getY(),"AlphabetPlay");
        render.drawText("ALFABETO", (int)(alfabetoInputButtonMode.getPos().getX() + alfabetoInputButtonMode.getSize().getX()/2), (int)(alfabetoInputButtonMode.getPos().getY() - alfabetoInputButtonMode.getSize().getY()/4), "Black","Cooper", 0,scaleWidth/18);

        //FIESTA
        render.drawImage((int)this.fiestaInputButtonMode.getPos().getX(), (int)this.fiestaInputButtonMode.getPos().getY(),(int)this.fiestaInputButtonMode.getSize().getX(),(int)this.fiestaInputButtonMode.getSize().getY(),"PartyPlay");
        render.drawText("FIESTA", (int)(fiestaInputButtonMode.getPos().getX() + fiestaInputButtonMode.getSize().getX()/2), (int)(fiestaInputButtonMode.getPos().getY() - fiestaInputButtonMode.getSize().getY()/4), "Black","Cooper", 0,scaleWidth/18);

        //ANIMALES
        render.drawImage((int)this.animalesInputButtonMode.getPos().getX(), (int)this.animalesInputButtonMode.getPos().getY(),(int)this.animalesInputButtonMode.getSize().getX(),(int)this.animalesInputButtonMode.getSize().getY(),"AnimalsPlay");
        render.drawText("ANIMALES", (int)(animalesInputButtonMode.getPos().getX() + animalesInputButtonMode.getSize().getX()/2), (int)(animalesInputButtonMode.getPos().getY() - animalesInputButtonMode.getSize().getY()/4), "Black","Cooper", 0,scaleWidth/18);

        //GEOMETRIA
        render.drawImage((int)this.geometriaInputButtonMode.getPos().getX(), (int)this.geometriaInputButtonMode.getPos().getY(),(int)this.geometriaInputButtonMode.getSize().getX(),(int)this.geometriaInputButtonMode.getSize().getY(),"GeometryPlay");
        render.drawText("GEOMETRIA", (int)(geometriaInputButtonMode.getPos().getX() + geometriaInputButtonMode.getSize().getX()/2), (int)(geometriaInputButtonMode.getPos().getY() - geometriaInputButtonMode.getSize().getY()/4), "Black","Cooper", 0,scaleWidth/18);
        //----------------------------------------

        //Back Button
        render.drawImage((int) backInputButton.getPos().getX(),(int) backInputButton.getPos().getY(),(int)(backInputButton.getSize().getX()),(int)(backInputButton.getSize().getY()), "Back");

        //Texto indicativo
        render.drawText("Elige la categoria que quieres jugar", (int)(scaleWidth/2.0), (int)(scaleHeight/5.4), "Black", "Cooper", 0,scaleWidth/18);

        //Moneas
        //MONEDAS
        render.drawText(Integer.toString(coins.get()), scaleWidth - coinSize-scaleWidth/100, (int)(scaleHeight/72 + coinSize/2.5f), "Black", "CooperBold", 1,scaleWidth/14);
        render.drawImage(scaleWidth-coinSize -scaleWidth/100, (int)(scaleHeight/72.0),coinSize,coinSize/2,"Coin");

    }

    @Override
    public void handleInput(EventHandler.EventType type, AdManager adManager, InputAndroid input, SceneMngrAndroid sceneMngr, AudioAndroid audio, RenderAndroid render){
        //Tetarracas
        if (input.inputReceived(this.alfabetoInputButtonMode.getPos(), this.alfabetoInputButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(context, this.progress[0], 1, this.coins,this.palettes, "alphabet");
            sceneMngr.pushScene(scene);
        }
        //Bubalongas
        if (input.inputReceived(this.fiestaInputButtonMode.getPos(), this.fiestaInputButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(context, this.progress[1], 2, this.coins,this.palettes, "party");
            sceneMngr.pushScene(scene);
        }
        //Bakugans
        if (input.inputReceived(this.animalesInputButtonMode.getPos(), this.animalesInputButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(context, this.progress[2], 3, this.coins,this.palettes, "animals");
            sceneMngr.pushScene(scene);
        }
        //Mamelungas
        if (input.inputReceived(this.geometriaInputButtonMode.getPos(), this.geometriaInputButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(context, this.progress[3], 4, this.coins,this.palettes, "geometry");
            sceneMngr.pushScene(scene);
        }

        //Back button
        if (input.inputReceived(this.backInputButton.getPos(), this.backInputButton.getSize())){
            sceneMngr.popScene();
        }
    }

    //Se llama cada vez que se gira la orientacion de la pantalla
    @Override
    public void configurationChanged(int orientation) {

    }
}