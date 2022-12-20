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
import com.example.engineandroid.Vector2D;

import java.util.concurrent.atomic.AtomicReference;

public class ThemeModeLevels implements Scene {

    //TODO Aqui guarda la relacion
    int scaleWidth, scaleHeight;

    private InputButton[] lvls;

    private String[] lvlImages;

    private InputButton backInputButton;

    private final AtomicReference<Integer> coins;
    private Integer coinSize;

    private final String[] categories = { "Alphabet", "Party", "Animals", "Geometry"};
    private final String selectedCategory;
    private final int category;

    private final AtomicReference<Integer> unlockedlevels;
    private final AtomicReference<Integer>[] palettes;

    private String folderName;

    Context context;


    public ThemeModeLevels(Context context, AtomicReference<Integer> levelsUnlocked, int selectedCategory, AtomicReference<Integer> coinsAux, AtomicReference<Integer>[] palettesAux, String folder_){
        this.context = context;
        this.selectedCategory = this.categories[selectedCategory - 1];
        this.category = selectedCategory;
        this.unlockedlevels = levelsUnlocked;
        this.palettes = palettesAux;

        this.coins = coinsAux;
        //Por defecto la escala es 1000x1000 pero creamos un setter por si alguien quiere alguna modificacion
        scaleHeight=1000;
        scaleWidth=1000;
        folderName = folder_;

        init();
    }

    @Override
    public void init() {
        coinSize = scaleWidth/10;
        //Botones selectores del nivel
        this.lvls = new InputButton[20];
        int contador = 0;
        for (int i = 0; i < 5; ++i){
            for (int j = 0; j < 4; ++j){
                this.lvls[contador] = new InputButton(scaleWidth/25 + (scaleWidth/25) * j  + (scaleWidth/5) * j,
                        (scaleHeight/4 + scaleHeight/48) + (scaleHeight/8 * i) + ((scaleHeight)/48 * i), scaleWidth/5, scaleHeight/8);
                contador++;
            }
        }

        this.lvlImages = new String[20];
        //AAAAAAAAAAAAAAAAAA Asignar imagen distinta dependiendo del nivel
        for (int i = 1; i < this.unlockedlevels.get(); ++i){
            this.lvlImages[i-1] = selectedCategory + "Play";
        }
        //AAAAAAAAAAAAAAAAAAA Asignar imagen de desbloqueado
        if (this.unlockedlevels.get() < this.lvlImages.length){
            this.lvlImages[this.unlockedlevels.get()-1] = selectedCategory +"Unlocked";
        }
        //AAAAAAAAAAAAAAAAAAA Asignar imagen de bloqueado
        for (int i = this.unlockedlevels.get() + 1; i < this.lvlImages.length+1; ++i){
            this.lvlImages[i-1] = selectedCategory + "Level";
        }

        this.backInputButton = new InputButton(10, 10, scaleWidth/10, scaleHeight/15);

        //TODO LevelsUnlocked es un int que le pasas de la escena anterior. Me desbloquea los niveles hasta ahi y el resto se ven
        //TODO de otra manera y no puedes interactuar con ellos
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
        for (int i = 0; i < lvls.length; ++i){
            render.drawImage((int)this.lvls[i].getPos().getX(), (int)this.lvls[i].getPos().getY(),(int)this.lvls[i].getSize().getX(),(int)this.lvls[i].getSize().getY(),lvlImages[i]);
        }

        //----------------------------------------

        //Back Button
        render.drawImage((int) backInputButton.getPos().getX(),(int) backInputButton.getPos().getY(),(int)(backInputButton.getSize().getX()),(int)(backInputButton.getSize().getY()), "Back");

        //Texto indicativo
        render.drawText(selectedCategory + " Category", scaleWidth/2, scaleHeight/8, "Black", "CooperBold", 0,scaleWidth/14);

        //Moneas
        //MONEDAS
        render.drawText(Integer.toString(coins.get()), scaleWidth- coinSize-scaleWidth/100, (int)(scaleHeight/72 + coinSize/2.5), "Black", "CooperBold", 1,scaleWidth/14);
        render.drawImage(scaleWidth-coinSize -scaleWidth/100, (int)(scaleHeight/72.0),coinSize,coinSize/2,"Coin");

    }

    @Override
    public void handleInput(EventHandler.EventType type, AdManager adManager, InputAndroid input, SceneMngrAndroid sceneMngr, AudioAndroid audio, RenderAndroid render){
        for (int i = 0; i < this.unlockedlevels.get(); ++i){
            if (input.inputReceived(this.lvls[i].getPos(), this.lvls[i].getSize())){
                HistoryModeGameScene playScene = new HistoryModeGameScene(context, "level" + (i+1),this.category, this.coins, this.unlockedlevels, i + 1,this.palettes, folderName);
                sceneMngr.pushScene(playScene);
            }
        }

        //Back button
        if (input.inputReceived(this.backInputButton.getPos(), this.backInputButton.getSize())){
            sceneMngr.popScene();
        }
    }

    //Se llama cuando la escena posterior se elimina y se vuelve aqui, por si hay que actualizar algo
    @Override
    public void onResume() {
        init();
    }
}
