package com.example.logica;

import com.example.lib.Engine;
import com.example.lib.IEventHandler;
import com.example.lib.IFont;
import com.example.lib.Scene;
import com.example.lib.Vector2D;

import java.awt.geom.Point2D;
import java.util.HashMap;

public class LevelSelection implements Scene {

    private Button button5;
    private Button button8;
    private Button button10;
    private Button backButton;

    private Engine engine;

    HashMap<String, IFont> fonts;

    public LevelSelection(Engine engineAux, HashMap<String, IFont> fontsAux){
        this.engine = engineAux;

        this.fonts = fontsAux;

        this.button5 = new Button(100, 400, 70, 50);
        this.button8 = new Button(250, 400, 70, 50);
        this.button10 = new Button(400, 400, 70, 50);
        this.backButton = new Button(20, 30, 100, 50);

    }

    public boolean inputReceived(Vector2D pos, Vector2D size){
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getRawCoords().getX(), engine.getInput().getRawCoords().getY());

        return (coords.getX() >= pos.getX() && coords.getX() <= pos.getX() + size.getX() &&
                coords.getY() >= pos.getY() && coords.getY() <= pos.getY() + size.getY());
    }

    @Override
    public void update(double deltaTime){
        if(engine.getEventMngr().getEvent().eventType != IEventHandler.EventType.NONE) {
            handleInput();
            engine.getEventMngr().sendEvent(IEventHandler.EventType.NONE);
        }
    }

    @Override
    public void render(){
        this.engine.drawText("5x5", (int)(button5.getPos().getX() + button5.getSize().getX()/3.5), (int)(button5.getPos().getY() + button5.getSize().getY()/2), "Black", fonts.get("Calibri"));
        this.engine.paintCell((int)button5.getPos().getX(), (int)button5.getPos().getY(), (int)(button5.getSize().getX()), (int)(button5.getSize().getY()), -1);
        this.engine.drawText("8x8", (int)(button8.getPos().getX() + button8.getSize().getX()/3.5), (int)(button8.getPos().getY() + button8.getSize().getY()/2), "Black", fonts.get("Calibri"));
        this.engine.paintCell((int)button8.getPos().getX(), (int)button8.getPos().getY(), (int)(button8.getSize().getX()), (int)(button8.getSize().getY()), -1);
        this.engine.drawText("10x10", (int)(button10.getPos().getX() + button10.getSize().getX()/5), (int)(button10.getPos().getY() + button10.getSize().getY()/2), "Black", fonts.get("Calibri"));
        this.engine.paintCell((int)button10.getPos().getX(), (int)button10.getPos().getY(), (int)(button10.getSize().getX()), (int)(button10.getSize().getY()), -1);
        this.engine.drawText("Volver", (int)(backButton.getPos().getX() + backButton.getSize().getX()/5), (int)(backButton.getPos().getY() + backButton.getSize().getY()/2), "Black", fonts.get("Calibri"));
        //Texto indicativo
        this.engine.drawText("Selecciona el tamaño del puzzle", 40, 200, "Black", fonts.get("Calibri"));
    }

    @Override
    public void handleInput(){
        if (inputReceived(this.button5.getPos(), this.button5.getSize())){
            MyScene playScene = new MyScene(this.engine, 5, 5, this.fonts);
            this.engine.setScene(playScene);
        }

        if (inputReceived(this.button8.getPos(), this.button8.getSize())){
            MyScene playScene = new MyScene(this.engine, 8, 8, this.fonts);
            this.engine.setScene(playScene);
        }

        if (inputReceived(this.button10.getPos(), this.button10.getSize())){
            MyScene playScene = new MyScene(this.engine, 10, 10, this.fonts);
            this.engine.setScene(playScene);
        }
    }
}
