package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

public class ThemeModeLevels implements Scene {


    private Button lvl1;
    private Button lvl2;
    private Button lvl3;
    private Button lvl4;

    private Button backButton;

    private EngineApp engine;

    private Integer coins = 0;
    private Integer coinSize;

    public ThemeModeLevels(EngineApp engineAux,int levelsUnlocked){
        this.engine = engineAux;
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
        this.lvl1 = new Button(engine.getWidth()/4  - engine.getWidth()/8, engine.getHeight()/6, engine.getWidth()/4, engine.getHeight()/6);
        this.lvl2 = new Button(engine.getWidth()*3/4 - engine.getWidth()/8, engine.getHeight()/3, engine.getWidth()/4, engine.getHeight()/6);
        this.lvl4 = new Button(engine.getWidth()/4 - engine.getWidth()/8, engine.getHeight()/3, engine.getWidth()/4, engine.getHeight()/6);
        this.lvl3 = new Button(engine.getWidth()*3/4 - engine.getWidth()/8, engine.getHeight()/6, engine.getWidth()/4, engine.getHeight()/6);

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
        //Lvl1
        this.engine.drawImage((int)this.lvl1.getPos().getX(), (int)this.lvl1.getPos().getY(),(int)this.lvl1.getSize().getX(),(int)this.lvl1.getSize().getY(),"Square");

        //Lvl2
        this.engine.drawImage((int)this.lvl2.getPos().getX(), (int)this.lvl2.getPos().getY(),(int)this.lvl2.getSize().getX(),(int)this.lvl2.getSize().getY(),"Square");

        //Lvl3
        this.engine.drawImage((int)this.lvl3.getPos().getX(), (int)this.lvl3.getPos().getY(),(int)this.lvl3.getSize().getX(),(int)this.lvl3.getSize().getY(),"Square");

        //Lvl4
        this.engine.drawImage((int)this.lvl4.getPos().getX(), (int)this.lvl4.getPos().getY(),(int)this.lvl4.getSize().getX(),(int)this.lvl4.getSize().getY(),"Square");

        //----------------------------------------

        //Back Button
        this.engine.drawImage((int)backButton.getPos().getX(),(int)backButton.getPos().getY(),(int)(backButton.getSize().getX()),(int)(backButton.getSize().getY()), "Back");

        //Texto indicativo
        this.engine.drawText("Categoria de mi pepe", (int)(engine.getWidth()/2), (int)(engine.getHeight()/8), "Black", "Amor", 0);

        //Moneas
        //MONEDAS
        this.engine.drawText(Integer.toString(coins), engine.getWidth() - coinSize-10, (int)engine.getHeight()/15, "Black", "CooperBold", 1);
        this.engine.drawImage(engine.getWidth()-coinSize -10, (int)engine.getHeight()/72,coinSize,coinSize,"Coin");

    }

    public void handleInput(){
        //Tetarracas
        if (inputReceived(this.lvl1.getPos(), this.lvl1.getSize())){
            HistoryModeGameScene playScene = new HistoryModeGameScene(this.engine, 5, 5, "level1",1);
            this.engine.setScene(playScene);
        }

        //Mamelungas
        if (inputReceived(this.lvl2.getPos(), this.lvl2.getSize())){
            HistoryModeGameScene playScene = new HistoryModeGameScene(this.engine, 8, 8,"level1",2);
            this.engine.setScene(playScene);
        }
        //Bubalongas
        if (inputReceived(this.lvl3.getPos(), this.lvl3.getSize())){
            HistoryModeGameScene playScene = new HistoryModeGameScene(this.engine, 5, 5,"level1",3);
            this.engine.setScene(playScene);
        }
        //Bakugans
        if (inputReceived(this.lvl4.getPos(), this.lvl4.getSize())){
            HistoryModeGameScene playScene = new HistoryModeGameScene(this.engine, 5, 5,"level1",4);
            this.engine.setScene(playScene);
        }

        //Back button
        if (inputReceived(this.backButton.getPos(), this.backButton.getSize())){
            this.engine.popScene();
        }
    }
}
