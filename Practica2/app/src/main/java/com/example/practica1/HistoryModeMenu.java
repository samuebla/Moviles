package com.example.practica1;

import com.example.engineandroid.AdManager;
import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.RenderAndroid;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.util.concurrent.atomic.AtomicReference;

public class HistoryModeMenu implements Scene {

    //TODO Aqui guarda la relacion
    int scaleWidth, scaleHeight;

    private InputButton themeInputButtonMode;
    private InputButton dificultyInputButtonMode;

    private InputButton backInputButton;

    AtomicReference<Integer> coins;
    private Integer coinSize;

    private final AtomicReference<Integer>[] progress;
    private final AtomicReference<Integer>[] palettes;

    private final EngineApp engine;

    public HistoryModeMenu(EngineApp engineAux, AtomicReference<Integer> coinsAux, AtomicReference<Integer>[] progressAux,AtomicReference<Integer>[] palettesAux){

        this.engine = engineAux;

        this.coins = coinsAux;
        this.progress = progressAux;
        this.palettes = palettesAux;

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
//        loadCoinsFromFile();
        //Botones selectores del nivel
        this.themeInputButtonMode = new InputButton(scaleWidth/4  - scaleWidth/8, scaleHeight/2, scaleWidth/4, scaleHeight/6);
        this.dificultyInputButtonMode = new InputButton(scaleWidth*3/4 - scaleWidth/8, scaleHeight/2, scaleWidth/4, scaleHeight/6);
        this.backInputButton = new InputButton(10,10, scaleWidth/10, scaleHeight/15);
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
    public void handleInput(EventHandler.EventType type, AdManager adManager){
        //ThemeMode
        if (inputReceived(this.themeInputButtonMode.getPos(), this.themeInputButtonMode.getSize())){
            ThemeModeMenu playScene = new ThemeModeMenu(this.engine, this.coins, this.progress,this.palettes);
            this.engine.getSceneMngr().pushScene(playScene);
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
