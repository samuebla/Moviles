package com.example.practica1;

import android.widget.Button;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

public class ThemeModeLevels implements Scene, Serializable {

    private InputButton[] lvls;

    private String[] lvlImages;

    private InputButton backInputButton;

    private EngineApp engine;

    private AtomicReference<Integer> coins;
    private Integer coinSize;

    private String[] categories = { "Alphabet", "Party", "Animals", "Geometry"};
    private String selectedCategory;
    private int category;

    private Button rewardButton;

    private AtomicReference<Integer> unlockedlevels;


    public ThemeModeLevels(EngineApp engineAux,AtomicReference<Integer> levelsUnlocked, int selectedCategory, AtomicReference<Integer> coinsAux, Button rewardButtonAux){
        this.engine = engineAux;
        this.selectedCategory = this.categories[selectedCategory - 1];
        this.category = selectedCategory;
        this.unlockedlevels = levelsUnlocked;
        this.coins = coinsAux;
        this.rewardButton = rewardButtonAux;
        init();
    }

    public boolean inputReceived(Vector2D pos, Vector2D size){
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), engine.getInput().getScaledCoords().getY());

        return (coords.getX() >= pos.getX() && coords.getX() <= pos.getX() + size.getX() &&
                coords.getY() >= pos.getY() && coords.getY() <= pos.getY() + size.getY());
    }

    public void init() {
        coinSize = engine.getGraphics().getWidth()/10;
        //Botones selectores del nivel
        this.lvls = new InputButton[20];
        int contador = 0;
        for (int i = 0; i < 5; ++i){
            for (int j = 0; j < 4; ++j){
                this.lvls[contador] = new InputButton(engine.getGraphics().getWidth()/25 + (engine.getGraphics().getWidth()/25) * j  + (engine.getGraphics().getWidth()/5) * j,
                        (engine.getGraphics().getHeight()/4 + engine.getGraphics().getHeight()/48) + (engine.getGraphics().getHeight()/8 * i) + (engine.getGraphics().getHeight()/48 * i), engine.getGraphics().getWidth()/5, engine.getGraphics().getHeight()/8);
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

        this.backInputButton = new InputButton(10 + engine.getGraphics().getWidth()/44, 30, engine.getGraphics().getWidth()/10, engine.getGraphics().getHeight()/15);

        //TODO LevelsUnlocked es un int que le pasas de la escena anterior. Me desbloquea los niveles hasta ahi y el resto se ven
        //TODO de otra manera y no puedes interactuar con ellos
    }

    @Override
    public void loadResources(EngineApp engineAux) {

    }

    public void update(double deltaTime){
        //Para los eventos...
        if(engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput(engine.getEventMngr().getEventType());
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
        this.engine.getGraphics().drawText(selectedCategory + " Category", (int)(engine.getGraphics().getWidth()/2), (int)(engine.getGraphics().getHeight()/8), "Black", "CooperBold", 0);

        //Moneas
        //MONEDAS
        this.engine.getGraphics().drawText(Integer.toString(coins.get()), engine.getGraphics().getWidth() - coinSize-10, (int)(engine.getGraphics().getHeight()/72 + coinSize/1.7f), "Black", "CooperBold", 1);
        this.engine.getGraphics().drawImage(engine.getGraphics().getWidth()-coinSize -10, (int)engine.getGraphics().getHeight()/72,coinSize,coinSize,"Coin");

    }

    @Override
    public void handleInput(EventHandler.EventType type){
        for (int i = 0; i < this.unlockedlevels.get(); ++i){
            if (inputReceived(this.lvls[i].getPos(), this.lvls[i].getSize())){
                HistoryModeGameScene playScene = new HistoryModeGameScene(this.engine, "level" + (i+1),this.category, this.coins, this.unlockedlevels, i + 1, this.rewardButton);
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
