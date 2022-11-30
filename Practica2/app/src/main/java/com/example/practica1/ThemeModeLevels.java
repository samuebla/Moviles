package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.util.concurrent.atomic.AtomicReference;

public class ThemeModeLevels implements Scene {

    private Button[] lvls;

    private String[] lvlImages;

    private Button backButton;

    private EngineApp engine;

    private AtomicReference<Integer> coins;
    private Integer coinSize;

    private String[] categories = { "QUEER", "TETAS", "FRUTAS", "CAPITALISMO"};
    private String selectedCategory;
    private int category;

    private Integer unlockedlevels;


    public ThemeModeLevels(EngineApp engineAux,Integer levelsUnlocked, int selectedCategory, AtomicReference<Integer> coinsAux){
        this.engine = engineAux;
        this.selectedCategory = this.categories[selectedCategory - 1];
        this.category = selectedCategory;
        this.unlockedlevels = levelsUnlocked;
        this.coins = coinsAux;
        coinSize = engine.getWidth()/10;
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
        this.lvls = new Button[20];
        int contador = 0;
        for (int i = 0; i < 5; ++i){
            for (int j = 0; j < 4; ++j){
                this.lvls[contador] = new Button(engine.getWidth()/25 + (engine.getWidth()/25) * j  + (engine.getWidth()/5) * j,
                        (engine.getHeight()/4 + engine.getHeight()/48) + (engine.getHeight()/8 * i) + (engine.getHeight()/48 * i), engine.getWidth()/5, engine.getHeight()/8);
                contador++;
            }
        }

        this.lvlImages = new String[20];
        //AAAAAAAAAAAAAAAAAA Asignar imagen distinta dependiendo del nivel
        for (int i = 0; i < this.unlockedlevels; ++i){
            this.lvlImages[i] = "Square";
        }
        //AAAAAAAAAAAAAAAAAAA Asignar imagen de desbloqueado
        if (this.unlockedlevels < this.lvlImages.length){
            this.lvlImages[this.unlockedlevels] = "Square";
        }
        //AAAAAAAAAAAAAAAAAAA Asignar imagen de bloqueado
        for (int i = this.unlockedlevels + 1; i < this.lvlImages.length; ++i){
            this.lvlImages[i] = "Square";
        }

        this.backButton = new Button(10 + engine.getWidth()/44, 30, engine.getWidth()/10, engine.getHeight()/15);

        //TODO LevelsUnlocked es un int que le pasas de la escena anterior. Me desbloquea los niveles hasta ahi y el resto se ven
        //TODO de otra manera y no puedes interactuar con ellos
    }

    public void update(double deltaTime){
        //Para los eventos...
        if(engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput();
            engine.getEventMngr().sendEvent(EventHandler.EventType.NONE);
        }
    }

    public void render(){
        for (int i = 0; i < lvls.length; ++i){
            this.engine.drawImage((int)this.lvls[i].getPos().getX(), (int)this.lvls[i].getPos().getY(),(int)this.lvls[i].getSize().getX(),(int)this.lvls[i].getSize().getY(),lvlImages[i]);
        }

        //----------------------------------------

        //Back Button
        this.engine.drawImage((int)backButton.getPos().getX(),(int)backButton.getPos().getY(),(int)(backButton.getSize().getX()),(int)(backButton.getSize().getY()), "Back");

        //Texto indicativo
        this.engine.drawText("Categoria de mi pepe", (int)(engine.getWidth()/2), (int)(engine.getHeight()/8), "Black", "Amor", 0);

        //Moneas
        //MONEDAS
        this.engine.drawText(Integer.toString(coins.get()), engine.getWidth() - coinSize-10, (int)engine.getHeight()/15, "Black", "CooperBold", 1);
        this.engine.drawImage(engine.getWidth()-coinSize -10, (int)engine.getHeight()/72,coinSize,coinSize,"Coin");

    }

    public void handleInput(){
        for (int i = 0; i < this.unlockedlevels; ++i){
            if (inputReceived(this.lvls[i].getPos(), this.lvls[i].getSize())){
                //AAAAAAAAAAAAAAAAAAAA Cambiar level1 por "level" + (i + 1)
                HistoryModeGameScene playScene = new HistoryModeGameScene(this.engine, 5, 5, "level1",this.category, this.coins);
                this.engine.setScene(playScene);
            }
        }

        //Back button
        if (inputReceived(this.backButton.getPos(), this.backButton.getSize())){
            this.engine.popScene();
        }
    }
}
