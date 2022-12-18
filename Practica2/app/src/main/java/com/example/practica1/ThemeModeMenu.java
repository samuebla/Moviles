package com.example.practica1;

import android.widget.Button;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

public class ThemeModeMenu implements Scene, Serializable {
    //TODO Aqui guarda la relacion
    int scaleWidth, scaleHeight;

    private InputButton alfabetoInputButtonMode;
    private InputButton geometriaInputButtonMode;
    private InputButton fiestaInputButtonMode;
    private InputButton animalesInputButtonMode;

    private InputButton backInputButton;

    private EngineApp engine;

    private AtomicReference<Integer> coins;
    private Integer coinSize;

    private AtomicReference<Integer>[] progress;

    public ThemeModeMenu(EngineApp engineAux, AtomicReference<Integer> coinsAux, AtomicReference<Integer>[] progressAux){
        this.engine = engineAux;
        this.coins = coinsAux;
        this.progress = progressAux;
        //Por defecto la escala es 1000x1000 pero podemos cambiarlo (recordar llamar al engine para cambiarlo)
        scaleHeight=1000;
        scaleWidth=1000;
        init();
    }

    public boolean inputReceived(Vector2D pos, Vector2D size){
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), engine.getInput().getScaledCoords().getY());

        return (coords.getX()*scaleWidth/engine.getGraphics().getWidth() >= pos.getX()  && coords.getX()*scaleWidth/engine.getGraphics().getWidth() <= pos.getX() + size.getX() &&
                coords.getY()*scaleHeight/engine.getGraphics().getHeight() >= pos.getY() && coords.getY()*scaleHeight/engine.getGraphics().getHeight() <= pos.getY() + size.getY());
    }

    public void init() {
        coinSize = scaleWidth/10;
        //Botones selectores del nivel
        this.alfabetoInputButtonMode = new InputButton(scaleWidth/4  - scaleWidth/8, scaleHeight/2.4, scaleWidth/4, scaleHeight/6);
        this.geometriaInputButtonMode = new InputButton(scaleWidth*3/4 - scaleWidth/8, scaleHeight/1.5, scaleWidth/4, scaleHeight/6);
        this.animalesInputButtonMode = new InputButton(scaleWidth/4 - scaleWidth/8, scaleHeight/1.5, scaleWidth/4, scaleHeight/6);
        this.fiestaInputButtonMode = new InputButton(scaleWidth*3/4 - scaleWidth/8, scaleHeight/2.4, scaleWidth/4, scaleHeight/6);



        this.backInputButton = new InputButton(10,10, scaleWidth/10, scaleHeight/15);
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
        //ALFABETO
        this.engine.getGraphics().drawImage((int)this.alfabetoInputButtonMode.getPos().getX(), (int)this.alfabetoInputButtonMode.getPos().getY(),(int)this.alfabetoInputButtonMode.getSize().getX(),(int)this.alfabetoInputButtonMode.getSize().getY(),"AlphabetPlay");
        this.engine.getGraphics().drawText("ALFABETO", (int)(alfabetoInputButtonMode.getPos().getX() + alfabetoInputButtonMode.getSize().getX()/2), (int)(alfabetoInputButtonMode.getPos().getY() - alfabetoInputButtonMode.getSize().getY()/4), "Black","Cooper", 0);

        //FIESTA
        this.engine.getGraphics().drawImage((int)this.fiestaInputButtonMode.getPos().getX(), (int)this.fiestaInputButtonMode.getPos().getY(),(int)this.fiestaInputButtonMode.getSize().getX(),(int)this.fiestaInputButtonMode.getSize().getY(),"PartyPlay");
        this.engine.getGraphics().drawText("FIESTA", (int)(fiestaInputButtonMode.getPos().getX() + fiestaInputButtonMode.getSize().getX()/2), (int)(fiestaInputButtonMode.getPos().getY() - fiestaInputButtonMode.getSize().getY()/4), "Black","Cooper", 0);

        //ANIMALES
        this.engine.getGraphics().drawImage((int)this.animalesInputButtonMode.getPos().getX(), (int)this.animalesInputButtonMode.getPos().getY(),(int)this.animalesInputButtonMode.getSize().getX(),(int)this.animalesInputButtonMode.getSize().getY(),"AnimalsPlay");
        this.engine.getGraphics().drawText("ANIMALES", (int)(animalesInputButtonMode.getPos().getX() + animalesInputButtonMode.getSize().getX()/2), (int)(animalesInputButtonMode.getPos().getY() - animalesInputButtonMode.getSize().getY()/4), "Black","Cooper", 0);

        //GEOMETRIA
        this.engine.getGraphics().drawImage((int)this.geometriaInputButtonMode.getPos().getX(), (int)this.geometriaInputButtonMode.getPos().getY(),(int)this.geometriaInputButtonMode.getSize().getX(),(int)this.geometriaInputButtonMode.getSize().getY(),"GeometryPlay");
        this.engine.getGraphics().drawText("GEOMETRIA", (int)(geometriaInputButtonMode.getPos().getX() + geometriaInputButtonMode.getSize().getX()/2), (int)(geometriaInputButtonMode.getPos().getY() - geometriaInputButtonMode.getSize().getY()/4), "Black","Cooper", 0);
        //----------------------------------------

        //Back Button
        this.engine.getGraphics().drawImage((int) backInputButton.getPos().getX(),(int) backInputButton.getPos().getY(),(int)(backInputButton.getSize().getX()),(int)(backInputButton.getSize().getY()), "Back");

        //Texto indicativo
        this.engine.getGraphics().drawText("Elige la categoria que quieres jugar", (int)(scaleWidth/2), (int)(scaleHeight/5.4), "Black", "Cooper", 0);

        //Moneas
        //MONEDAS
        this.engine.getGraphics().drawText(Integer.toString(coins.get()), scaleWidth - coinSize-scaleWidth/100, (int)(scaleHeight/72 + coinSize/2.5f), "Black", "CooperBold", 1);
        this.engine.getGraphics().drawImage(scaleWidth-coinSize -scaleWidth/100, (int)scaleHeight/72,coinSize,coinSize/2,"Coin");

    }

    @Override
    public void handleInput(EventHandler.EventType type){
        //Tetarracas
        if (inputReceived(this.alfabetoInputButtonMode.getPos(), this.alfabetoInputButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(engine,this.progress[0], 1, this.coins);
            this.engine.getSceneMngr().pushScene(scene);
        }
        //Bubalongas
        if (inputReceived(this.fiestaInputButtonMode.getPos(), this.fiestaInputButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(engine,this.progress[1], 2, this.coins);
            this.engine.getSceneMngr().pushScene(scene);
        }
        //Bakugans
        if (inputReceived(this.animalesInputButtonMode.getPos(), this.animalesInputButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(engine,this.progress[2], 3, this.coins);
            this.engine.getSceneMngr().pushScene(scene);
        }
        //Mamelungas
        if (inputReceived(this.geometriaInputButtonMode.getPos(), this.geometriaInputButtonMode.getSize())){
            ThemeModeLevels scene = new ThemeModeLevels(engine,this.progress[3], 4, this.coins);
            this.engine.getSceneMngr().pushScene(scene);
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