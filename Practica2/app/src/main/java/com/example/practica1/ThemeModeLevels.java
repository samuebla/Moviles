package com.example.practica1;

import android.widget.Button;

import com.example.engineandroid.AdManager;
import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import kotlinx.coroutines.ObsoleteCoroutinesApi;

public class ThemeModeLevels implements Scene, Serializable {

    //TODO Aqui guarda la relacion
    int scaleWidth, scaleHeight;

    private InputButton[] lvls;

    private String[] lvlImages;

    private InputButton backInputButton;

    private EngineApp engine;

    private AtomicReference<Integer> coins;
    private Integer coinSize;

    private String[] categories = { "Alphabet", "Party", "Animals", "Geometry"};
    private String selectedCategory;
    private int category;

    private AtomicReference<Integer> unlockedlevels;


    public ThemeModeLevels(EngineApp engineAux,AtomicReference<Integer> levelsUnlocked, int selectedCategory, AtomicReference<Integer> coinsAux){
        this.engine = engineAux;
        this.selectedCategory = this.categories[selectedCategory - 1];
        this.category = selectedCategory;
        this.unlockedlevels = levelsUnlocked;
        this.coins = coinsAux;
        //Por defecto la escala es 1000x1000 pero creamos un setter por si alguien quiere alguna modificacion
        scaleHeight=1000;
        scaleWidth=1000;
        init();
    }

    @Override
    public boolean inputReceived(Vector2D pos, Vector2D size){
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), engine.getInput().getScaledCoords().getY());

        return (coords.getX()*scaleWidth/engine.getGraphics().getWidth() >= pos.getX()  && coords.getX()*scaleWidth/engine.getGraphics().getWidth() <= pos.getX() + size.getX() &&
                coords.getY()*scaleHeight/engine.getGraphics().getHeight() >= pos.getY() && coords.getY()*scaleHeight/engine.getGraphics().getHeight() <= pos.getY() + size.getY());
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
    public void loadResources(EngineApp engineAux) {

    }

    @Override
    public void update(double deltaTime, AdManager adManager){
        //Para los eventos...
        if(engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput(engine.getEventMngr().getEventType(), adManager);
            engine.getEventMngr().sendEvent(EventHandler.EventType.NONE);
        }
    }

    public void render(){
        for (int i = 0; i < lvls.length; ++i){
            this.engine.getGraphics().drawImage((int)this.lvls[i].getPos().getX(), (int)this.lvls[i].getPos().getY(),(int)this.lvls[i].getSize().getX(),(int)this.lvls[i].getSize().getY(),lvlImages[i]);
        }

        //----------------------------------------

        //Back Button
        this.engine.getGraphics().drawImage((int) backInputButton.getPos().getX(),(int) backInputButton.getPos().getY(),(int)(backInputButton.getSize().getX()),(int)(backInputButton.getSize().getY()), "Back");

        //Texto indicativo
        this.engine.getGraphics().drawText(selectedCategory + " Category", (int)(scaleWidth/2), (int)(scaleHeight/8), "Black", "CooperBold", 0);

        //Moneas
        //MONEDAS
        this.engine.getGraphics().drawText(Integer.toString(coins.get()), scaleWidth- coinSize-scaleWidth/100, (int)(scaleHeight/72 + + coinSize/2.5), "Black", "CooperBold", 1);
        this.engine.getGraphics().drawImage(scaleWidth-coinSize -scaleWidth/100, (int)scaleHeight/72,coinSize,coinSize/2,"Coin");

    }

    @Override
    public void handleInput(EventHandler.EventType type, AdManager adManager){
        for (int i = 0; i < this.unlockedlevels.get(); ++i){
            if (inputReceived(this.lvls[i].getPos(), this.lvls[i].getSize())){
                HistoryModeGameScene playScene = new HistoryModeGameScene(this.engine, "level" + (i+1),this.category, this.coins, this.unlockedlevels, i + 1);
                this.engine.getSceneMngr().pushScene(playScene);
            }
        }

        //Back button
        if (inputReceived(this.backInputButton.getPos(), this.backInputButton.getSize())){
            this.engine.getSceneMngr().popScene();
        }
    }

    //Se llama cuando la escena posterior se elimina y se vuelve aqui, por si hay que actualizar algo
    @Override
    public void onResume() {
        init();
    }
}
