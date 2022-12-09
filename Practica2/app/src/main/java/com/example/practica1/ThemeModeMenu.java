package com.example.practica1;

import android.widget.Button;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.util.concurrent.atomic.AtomicReference;

public class ThemeModeMenu implements Scene {


    private InputButton tetarracasInputButtonMode;
    private InputButton mamelungasInputButtonMode;
    private InputButton bubalongasInputButtonMode;
    private InputButton bakugansInputButtonMode;

    private InputButton backInputButton;

    private EngineApp engine;

    private AtomicReference<Integer> coins;
    private Integer coinSize;

    private Button rewardButton;

    private AtomicReference<Integer>[] progress;

    public ThemeModeMenu(EngineApp engineAux, AtomicReference<Integer> coinsAux, AtomicReference<Integer>[] progressAux, Button rewardButtonAux){
        this.engine = engineAux;
        this.coins = coinsAux;
        this.progress = progressAux;
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
        this.tetarracasInputButtonMode = new InputButton(engine.getWidth()/4  - engine.getWidth()/8, engine.getHeight()/2.4, engine.getWidth()/4, engine.getHeight()/6);
        this.mamelungasInputButtonMode = new InputButton(engine.getWidth()*3/4 - engine.getWidth()/8, engine.getHeight()/1.5, engine.getWidth()/4, engine.getHeight()/6);
        this.bakugansInputButtonMode = new InputButton(engine.getWidth()/4 - engine.getWidth()/8, engine.getHeight()/1.5, engine.getWidth()/4, engine.getHeight()/6);
        this.bubalongasInputButtonMode = new InputButton(engine.getWidth()*3/4 - engine.getWidth()/8, engine.getHeight()/2.4, engine.getWidth()/4, engine.getHeight()/6);



        this.backInputButton = new InputButton(10 + engine.getWidth()/44, 30, engine.getWidth()/10, engine.getHeight()/15);
    }

    public void update(double deltaTime){
        //Para los eventos...
        if(engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput(engine.getEventMngr().getEventType());
            engine.getEventMngr().sendEvent(EventHandler.EventType.NONE);
        }
    }

    public void render(){
        //Tetarracas
        this.engine.drawImage((int)this.tetarracasInputButtonMode.getPos().getX(), (int)this.tetarracasInputButtonMode.getPos().getY(),(int)this.tetarracasInputButtonMode.getSize().getX(),(int)this.tetarracasInputButtonMode.getSize().getY(),"PlayButton");
        this.engine.drawText("Tetarracas", (int)(tetarracasInputButtonMode.getPos().getX() + tetarracasInputButtonMode.getSize().getX()/2), (int)(tetarracasInputButtonMode.getPos().getY() - tetarracasInputButtonMode.getSize().getY()/4), "Black","Amor", 0);

        //Mamelungas
        this.engine.drawImage((int)this.mamelungasInputButtonMode.getPos().getX(), (int)this.mamelungasInputButtonMode.getPos().getY(),(int)this.mamelungasInputButtonMode.getSize().getX(),(int)this.mamelungasInputButtonMode.getSize().getY(),"PlayButton");
        this.engine.drawText("Mmamelungas", (int)(mamelungasInputButtonMode.getPos().getX() + mamelungasInputButtonMode.getSize().getX()/2), (int)(mamelungasInputButtonMode.getPos().getY() - mamelungasInputButtonMode.getSize().getY()/4), "Black","Amor", 0);

        //Bubalongas
        this.engine.drawImage((int)this.bubalongasInputButtonMode.getPos().getX(), (int)this.bubalongasInputButtonMode.getPos().getY(),(int)this.bubalongasInputButtonMode.getSize().getX(),(int)this.bubalongasInputButtonMode.getSize().getY(),"PlayButton");
        this.engine.drawText("Bubalongas", (int)(bubalongasInputButtonMode.getPos().getX() + bubalongasInputButtonMode.getSize().getX()/2), (int)(bubalongasInputButtonMode.getPos().getY() - bubalongasInputButtonMode.getSize().getY()/4), "Black","Amor", 0);

        //Bakugans
        this.engine.drawImage((int)this.bakugansInputButtonMode.getPos().getX(), (int)this.bakugansInputButtonMode.getPos().getY(),(int)this.bakugansInputButtonMode.getSize().getX(),(int)this.bakugansInputButtonMode.getSize().getY(),"PlayButton");
        this.engine.drawText("Bakugans", (int)(bakugansInputButtonMode.getPos().getX() + bakugansInputButtonMode.getSize().getX()/2), (int)(bakugansInputButtonMode.getPos().getY() - bakugansInputButtonMode.getSize().getY()/4), "Black","Amor", 0);

        //----------------------------------------

        //Back Button
        this.engine.drawImage((int) backInputButton.getPos().getX(),(int) backInputButton.getPos().getY(),(int)(backInputButton.getSize().getX()),(int)(backInputButton.getSize().getY()), "Back");

        //Texto indicativo
        this.engine.drawText("Elige la categoria que quieres jugar", (int)(engine.getWidth()/2), (int)(engine.getHeight()/5.4), "Black", "Amor", 0);

        //Moneas
        //MONEDAS
        this.engine.drawText(Integer.toString(coins.get()), engine.getWidth() - coinSize-10, (int)engine.getHeight()/15, "Black", "CooperBold", 1);
        this.engine.drawImage(engine.getWidth()-coinSize -10, (int)engine.getHeight()/72,coinSize,coinSize,"Coin");

    }

    @Override
    public void handleInput(EventHandler.EventType type){
        //Tetarracas
        if (inputReceived(this.tetarracasInputButtonMode.getPos(), this.tetarracasInputButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(engine,this.progress[0], 1, this.coins, this.rewardButton);
            this.engine.setScene(scene);
        }
        //Mamelungas
        if (inputReceived(this.mamelungasInputButtonMode.getPos(), this.mamelungasInputButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(engine,this.progress[1], 2, this.coins, this.rewardButton);
            this.engine.setScene(scene);
        }
        //Bubalongas
        if (inputReceived(this.bubalongasInputButtonMode.getPos(), this.bubalongasInputButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(engine,this.progress[2], 3, this.coins, this.rewardButton);
            this.engine.setScene(scene);
        }
        //Bakugans
        if (inputReceived(this.bakugansInputButtonMode.getPos(), this.bakugansInputButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(engine,this.progress[3], 4, this.coins, this.rewardButton);
            this.engine.setScene(scene);
        }

        //Back button
        if (inputReceived(this.backInputButton.getPos(), this.backInputButton.getSize())){
            this.engine.popScene();
        }
    }
}