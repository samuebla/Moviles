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

public class HistoryModeMenu implements Scene {

    int scaleWidth, scaleHeight;

    private InputButton themeInputButtonMode;
    private InputButton dificultyInputButtonMode;

    private InputButton backInputButton;

    AtomicReference<Integer> coins;
    private Integer coinSize;

    private final AtomicReference<Integer>[] progress;
    private final AtomicReference<Integer>[] palettes;

    private final Context context;

    public HistoryModeMenu(Context context, AtomicReference<Integer> coinsAux, AtomicReference<Integer>[] progressAux, AtomicReference<Integer>[] palettesAux){
        this.context = context;
        this.coins = coinsAux;
        this.progress = progressAux;
        this.palettes = palettesAux;

        //Por defecto la escala es 1000x1000
        scaleHeight=1000;
        scaleWidth=1000;

        init();
    }

    @Override
    public void init() {
        coinSize = scaleWidth/10;
        //Botones selectores del nivel
        this.themeInputButtonMode = new InputButton(scaleWidth/4  - scaleWidth/8, scaleHeight/2, scaleWidth/4, scaleHeight/6);
        this.dificultyInputButtonMode = new InputButton(scaleWidth*3/4 - scaleWidth/8, scaleHeight/2, scaleWidth/4, scaleHeight/6);
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
        //ThemeMode
        render.drawImage((int)this.themeInputButtonMode.getPos().getX(), (int)this.themeInputButtonMode.getPos().getY(),(int)this.themeInputButtonMode.getSize().getX(),(int)this.themeInputButtonMode.getSize().getY(),"ThemePlay");
        render.drawText("Temático", (int)(themeInputButtonMode.getPos().getX() + themeInputButtonMode.getSize().getX()/2), (int)(themeInputButtonMode.getPos().getY() - themeInputButtonMode.getSize().getY()*0.5), "Black","Cooper", 0,scaleWidth/18);

        //DificultyMode
        render.drawImage((int)this.dificultyInputButtonMode.getPos().getX(), (int)this.dificultyInputButtonMode.getPos().getY(),(int)this.dificultyInputButtonMode.getSize().getX(),(int)this.dificultyInputButtonMode.getSize().getY(),"ChallengePlay");
        render.drawText("Dificultad", (int)(dificultyInputButtonMode.getPos().getX() + dificultyInputButtonMode.getSize().getX()/2), (int)(dificultyInputButtonMode.getPos().getY() - dificultyInputButtonMode.getSize().getY()*0.5), "Black","Cooper", 0,scaleWidth/18);

        //Back Button
        render.drawImage((int) backInputButton.getPos().getX(), (int) backInputButton.getPos().getY(),(int)(backInputButton.getSize().getX()),(int)(backInputButton.getSize().getY()), "Back");

        //Texto indicativo
        render.drawText("Selecciona el modo de Juego", (int)(scaleWidth/2.0), (int)(scaleHeight/5.4), "Black", "Cooper", 0,scaleWidth/18);

        //Moneda
        //MONEDAS
        render.drawText(Integer.toString(coins.get()),scaleWidth - coinSize-scaleWidth/100, (int)(scaleHeight/72 + coinSize/2.5 ), "Black", "CooperBold", 1,scaleWidth/14);
        render.drawImage(scaleWidth-coinSize -scaleWidth/100, (int)(scaleHeight/72.0),coinSize,coinSize/2,"Coin");
    }

    @Override
    public void handleInput(EventHandler.EventType type, AdManager adManager, InputAndroid input, SceneMngrAndroid sceneMngr, AudioAndroid audio, RenderAndroid render){
        //ThemeMode
        if (input.inputReceived(this.themeInputButtonMode.getPos(), this.themeInputButtonMode.getSize())){
            ThemeModeMenu playScene = new ThemeModeMenu(context, this.coins, this.progress,this.palettes);
            sceneMngr.pushScene(playScene);
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
