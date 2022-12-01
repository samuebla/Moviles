package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class ThemeModeMenu implements Scene {


    private Button tetarracasButtonMode;
    private Button mamelungasButtonMode;
    private Button bubalongasButtonMode;
    private Button bakugansButtonMode;

    private Button backButton;

    private EngineApp engine;

    private AtomicReference<Integer> coins;
    private Integer coinSize;

    private AtomicReference<Integer>[] progress;

    public ThemeModeMenu(EngineApp engineAux, AtomicReference<Integer> coinsAux, AtomicReference<Integer>[] progressAux){
        this.engine = engineAux;
        this.coins = coinsAux;
        this.progress = progressAux;
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
        this.tetarracasButtonMode = new Button(engine.getWidth()/4  - engine.getWidth()/8, engine.getHeight()/2.4, engine.getWidth()/4, engine.getHeight()/6);
        this.mamelungasButtonMode = new Button(engine.getWidth()*3/4 - engine.getWidth()/8, engine.getHeight()/1.5, engine.getWidth()/4, engine.getHeight()/6);
        this.bakugansButtonMode = new Button(engine.getWidth()/4 - engine.getWidth()/8, engine.getHeight()/1.5, engine.getWidth()/4, engine.getHeight()/6);
        this.bubalongasButtonMode = new Button(engine.getWidth()*3/4 - engine.getWidth()/8, engine.getHeight()/2.4, engine.getWidth()/4, engine.getHeight()/6);



        this.backButton = new Button(10 + engine.getWidth()/44, 30, engine.getWidth()/10, engine.getHeight()/15);
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
        this.engine.drawImage((int)this.tetarracasButtonMode.getPos().getX(), (int)this.tetarracasButtonMode.getPos().getY(),(int)this.tetarracasButtonMode.getSize().getX(),(int)this.tetarracasButtonMode.getSize().getY(),"PlayButton");
        this.engine.drawText("Tetarracas", (int)(tetarracasButtonMode.getPos().getX() + tetarracasButtonMode.getSize().getX()/2), (int)(tetarracasButtonMode.getPos().getY() - tetarracasButtonMode.getSize().getY()/4), "Black","Amor", 0);

        //Mamelungas
        this.engine.drawImage((int)this.mamelungasButtonMode.getPos().getX(), (int)this.mamelungasButtonMode.getPos().getY(),(int)this.mamelungasButtonMode.getSize().getX(),(int)this.mamelungasButtonMode.getSize().getY(),"PlayButton");
        this.engine.drawText("Mmamelungas", (int)(mamelungasButtonMode.getPos().getX() + mamelungasButtonMode.getSize().getX()/2), (int)(mamelungasButtonMode.getPos().getY() - mamelungasButtonMode.getSize().getY()/4), "Black","Amor", 0);

        //Bubalongas
        this.engine.drawImage((int)this.bubalongasButtonMode.getPos().getX(), (int)this.bubalongasButtonMode.getPos().getY(),(int)this.bubalongasButtonMode.getSize().getX(),(int)this.bubalongasButtonMode.getSize().getY(),"PlayButton");
        this.engine.drawText("Bubalongas", (int)(bubalongasButtonMode.getPos().getX() + bubalongasButtonMode.getSize().getX()/2), (int)(bubalongasButtonMode.getPos().getY() - bubalongasButtonMode.getSize().getY()/4), "Black","Amor", 0);

        //Bakugans
        this.engine.drawImage((int)this.bakugansButtonMode.getPos().getX(), (int)this.bakugansButtonMode.getPos().getY(),(int)this.bakugansButtonMode.getSize().getX(),(int)this.bakugansButtonMode.getSize().getY(),"PlayButton");
        this.engine.drawText("Bakugans", (int)(bakugansButtonMode.getPos().getX() + bakugansButtonMode.getSize().getX()/2), (int)(bakugansButtonMode.getPos().getY() - bakugansButtonMode.getSize().getY()/4), "Black","Amor", 0);

        //----------------------------------------

        //Back Button
        this.engine.drawImage((int)backButton.getPos().getX(),(int)backButton.getPos().getY(),(int)(backButton.getSize().getX()),(int)(backButton.getSize().getY()), "Back");

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
        if (inputReceived(this.tetarracasButtonMode.getPos(), this.tetarracasButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(engine,this.progress[0], 1, this.coins);
            this.engine.setScene(scene);
        }
        //Mamelungas
        if (inputReceived(this.mamelungasButtonMode.getPos(), this.mamelungasButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(engine,this.progress[1], 2, this.coins);
            this.engine.setScene(scene);
        }
        //Bubalongas
        if (inputReceived(this.bubalongasButtonMode.getPos(), this.bubalongasButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(engine,this.progress[2], 3, this.coins);
            this.engine.setScene(scene);
        }
        //Bakugans
        if (inputReceived(this.bakugansButtonMode.getPos(), this.bakugansButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(engine,this.progress[3], 4, this.coins);
            this.engine.setScene(scene);
        }

        //Back button
        if (inputReceived(this.backButton.getPos(), this.backButton.getSize())){
            this.engine.popScene();
        }
    }
}