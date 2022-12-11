package com.example.practica1;

import android.widget.Button;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.util.concurrent.atomic.AtomicReference;

public class ThemeModeLevels implements Scene {

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
        coinSize = engine.getWidth()/10;
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
        //Botones selectores del nivel
        this.lvls = new InputButton[20];
        int contador = 0;
        for (int i = 0; i < 5; ++i){
            for (int j = 0; j < 4; ++j){
                this.lvls[contador] = new InputButton(engine.getWidth()/25 + (engine.getWidth()/25) * j  + (engine.getWidth()/5) * j,
                        (engine.getHeight()/4 + engine.getHeight()/48) + (engine.getHeight()/8 * i) + (engine.getHeight()/48 * i), engine.getWidth()/5, engine.getHeight()/8);
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
            this.lvlImages[this.unlockedlevels.get()-1] = "Unlocked";
        }
        //AAAAAAAAAAAAAAAAAAA Asignar imagen de bloqueado
        for (int i = this.unlockedlevels.get() + 1; i < this.lvlImages.length+1; ++i){
            this.lvlImages[i-1] = selectedCategory + "Level";
        }

        this.backInputButton = new InputButton(10 + engine.getWidth()/44, 30, engine.getWidth()/10, engine.getHeight()/15);

        //TODO LevelsUnlocked es un int que le pasas de la escena anterior. Me desbloquea los niveles hasta ahi y el resto se ven
        //TODO de otra manera y no puedes interactuar con ellos
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
            this.engine.drawImage((int)this.lvls[i].getPos().getX(), (int)this.lvls[i].getPos().getY(),(int)this.lvls[i].getSize().getX(),(int)this.lvls[i].getSize().getY(),lvlImages[i]);
        }

        //----------------------------------------

        //Back Button
        this.engine.drawImage((int) backInputButton.getPos().getX(),(int) backInputButton.getPos().getY(),(int)(backInputButton.getSize().getX()),(int)(backInputButton.getSize().getY()), "Back");

        //Texto indicativo
        this.engine.drawText("Categoria de mi pepe", (int)(engine.getWidth()/2), (int)(engine.getHeight()/8), "Black", "Amor", 0);

        //Moneas
        //MONEDAS
        this.engine.drawText(Integer.toString(coins.get()), engine.getWidth() - coinSize-10, (int)engine.getHeight()/15, "Black", "CooperBold", 1);
        this.engine.drawImage(engine.getWidth()-coinSize -10, (int)engine.getHeight()/72,coinSize,coinSize,"Coin");

    }

    @Override
    public void handleInput(EventHandler.EventType type){
        for (int i = 0; i < this.unlockedlevels.get(); ++i){
            if (inputReceived(this.lvls[i].getPos(), this.lvls[i].getSize())){
                HistoryModeGameScene playScene = new HistoryModeGameScene(this.engine, 5, 5, "level" + (i+1),this.category, this.coins, this.unlockedlevels, i + 1, this.rewardButton);
                this.engine.setScene(playScene);
            }
        }

        //Back button
        if (inputReceived(this.backInputButton.getPos(), this.backInputButton.getSize())){
            this.engine.popScene();
        }
    }

    //Se llama cuando la escena posterior se elimina y se vuelve aqui, por si hay que actualizar algo
    @Override
    public void onResume() {
        for (int i = 1; i < this.unlockedlevels.get(); ++i){
            this.lvlImages[i-1] = selectedCategory + "Play";
        }
        if (this.unlockedlevels.get() < this.lvlImages.length){
            this.lvlImages[this.unlockedlevels.get()-1] = "Unlocked";
        }
    }
}
