package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

public class ThemeModeMenu implements Scene {


    private Button tetarracasButtonMode;
    private Button mamelungasButtonMode;
    private Button bubalongasButtonMode;
    private Button bakugansButtonMode;

    private Button backButton;

    private EngineApp engine;

    public ThemeModeMenu(EngineApp engineAux){
        this.engine = engineAux;
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
        this.tetarracasButtonMode = new Button(engine.getWidth()/4  - engine.getWidth()/8, engine.getHeight()/3.5, engine.getWidth()/4, engine.getHeight()/6);
        this.mamelungasButtonMode = new Button(engine.getWidth()*3/4 - engine.getWidth()/8, engine.getHeight()/1.5, engine.getWidth()/4, engine.getHeight()/6);
        this.bakugansButtonMode = new Button(engine.getWidth()/4 - engine.getWidth()/8, engine.getHeight()/1.5, engine.getWidth()/4, engine.getHeight()/6);
        this.bubalongasButtonMode = new Button(engine.getWidth()*3/4 - engine.getWidth()/8, engine.getHeight()/3.5, engine.getWidth()/4, engine.getHeight()/6);



        this.backButton = new Button(engine.getWidth()/72 + engine.getWidth()/44, engine.getHeight()/22, engine.getWidth()/10, engine.getHeight()/15);
    }

    public void update(double deltaTime){
        //Para los eventos...
        if(engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput();
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
        this.engine.drawImage(engine.getWidth()/72, engine.getHeight()/22,(int)(backButton.getSize().getX()),(int)(backButton.getSize().getY()), "Back");

        //Texto indicativo
        this.engine.drawText("Elige la categoria que quieres jugar", (int)(engine.getWidth()/2), (int)(engine.getHeight()/8), "Black", "Amor", 0);
    }

    public void handleInput(){
        //Tetarracas
        if (inputReceived(this.tetarracasButtonMode.getPos(), this.tetarracasButtonMode.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 5, 5);
            this.engine.setScene(playScene);
        }

        //Mamelungas
        if (inputReceived(this.mamelungasButtonMode.getPos(), this.mamelungasButtonMode.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 8, 8);
            this.engine.setScene(playScene);
        }
        //Bubalongas
        if (inputReceived(this.bubalongasButtonMode.getPos(), this.bubalongasButtonMode.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 5, 5);
            this.engine.setScene(playScene);
        }
        //Bakugans
        if (inputReceived(this.bakugansButtonMode.getPos(), this.bakugansButtonMode.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 5, 5);
            this.engine.setScene(playScene);
        }

        //Back button
        if (inputReceived(this.backButton.getPos(), this.backButton.getSize())){
            this.engine.popScene();
        }
    }
}