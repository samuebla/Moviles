package com.example.logica;

import com.example.lib.Engine;
import com.example.lib.IEventHandler;
import com.example.lib.IFont;
import com.example.lib.IImage;
import com.example.lib.ISound;
import com.example.lib.Scene;
import com.example.lib.Vector2D;

import java.awt.geom.Point2D;
import java.util.HashMap;

public class LevelSelection implements Scene {

    private Button button5;
    private Button button8;
    private Button button10;
    private Button button5x8;
    private Button button8x10;
    private Button button5x10;

    private Button backButton;

    private Engine engine;


    public LevelSelection(Engine engineAux){
        this.engine = engineAux;

        this.button5 = new Button(engine.getWidth()/4, engine.getHeight()/3.6, 70, 70);
        this.button8 = new Button(engine.getWidth()/2.2, engine.getHeight()/3.6, 70, 70);
        this.button10 = new Button(engine.getWidth()/1.5, engine.getHeight()/3.6, 70, 70);
        this.button5x8 = new Button(engine.getWidth()/4, engine.getHeight()/2.2, 70, 70);
        this.button8x10 = new Button(engine.getWidth()/2.2, engine.getHeight()/2.2, 70, 70);
        this.button5x10 = new Button(engine.getWidth()/1.5, engine.getHeight()/2.2, 70, 70);
        this.backButton = new Button(engine.getWidth()/72, engine.getHeight()/22, 120, 30);
    }

    @Override
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
        //5x5
        this.engine.drawText("5x5", (int)(button5.getPos().getX() + button5.getSize().getX()/3.8), (int)(button5.getPos().getY() + button5.getSize().getY()/1.8), "Black","Calibri");
        this.engine.paintCell((int)button5.getPos().getX(), (int)button5.getPos().getY(), (int)(button5.getSize().getX()), (int)(button5.getSize().getY()), -1);
        //8x8
        this.engine.drawText("8x8", (int)(button8.getPos().getX() + button8.getSize().getX()/3.8), (int)(button8.getPos().getY() + button8.getSize().getY()/1.8), "Black","Calibri");
        this.engine.paintCell((int)button8.getPos().getX(), (int)button8.getPos().getY(), (int)(button8.getSize().getX()), (int)(button8.getSize().getY()), -1);
        //10x10
        this.engine.drawText("10x10", (int)(button10.getPos().getX() + button10.getSize().getX()/9), (int)(button10.getPos().getY() + button10.getSize().getY()/1.8), "Black", "Calibri");
        this.engine.paintCell((int)button10.getPos().getX(), (int)button10.getPos().getY(), (int)(button10.getSize().getX()), (int)(button10.getSize().getY()), -1);

        this.engine.drawText("5x8", (int)(button5x8.getPos().getX() + button5x8.getSize().getX()/3.8), (int)(button5x8.getPos().getY() + button5x8.getSize().getY()/1.8), "Black", "Calibri");
        this.engine.paintCell((int)button5x8.getPos().getX(), (int)button5x8.getPos().getY(), (int)(button5x8.getSize().getX()), (int)(button5x8.getSize().getY()), -1);

        this.engine.drawText("8x10", (int)(button8x10.getPos().getX() + button8x10.getSize().getX()/5.5), (int)(button8x10.getPos().getY() + button8x10.getSize().getY()/1.8), "Black", "Calibri");
        this.engine.paintCell((int)button8x10.getPos().getX(), (int)button8x10.getPos().getY(), (int)(button8x10.getSize().getX()), (int)(button8x10.getSize().getY()), -1);

        this.engine.drawText("5x10", (int)(button5x10.getPos().getX() + button5x10.getSize().getX()/6), (int)(button5x10.getPos().getY() + button5x10.getSize().getY()/1.8), "Black", "Calibri");
        this.engine.paintCell((int)button5x10.getPos().getX(), (int)button5x10.getPos().getY(), (int)(button5x10.getSize().getX()), (int)(button5x10.getSize().getY()), -1);

        //Back Button
        this.engine.drawImage(engine.getWidth()/72, engine.getHeight()/22, 50, 75, "Flecha");
        this.engine.drawText("Volver", (int)(backButton.getPos().getX() +50), (int)(backButton.getPos().getY() + 20), "Black", "CalibriBold");

        //Texto indicativo
        this.engine.drawText("Selecciona el tamaño del puzzle", (int)(engine.getWidth()/3.6), (int)(engine.getHeight()/5.4), "Black", "Calibri");
    }

    @Override
    public void handleInput(){
        if (inputReceived(this.button5.getPos(), this.button5.getSize())){
            MyScene playScene = new MyScene(this.engine, 5, 5);
            this.engine.setScene(playScene);
        }

        if (inputReceived(this.button8.getPos(), this.button8.getSize())){
            MyScene playScene = new MyScene(this.engine, 8, 8);
            this.engine.setScene(playScene);
        }

        if (inputReceived(this.button10.getPos(), this.button10.getSize())){
            MyScene playScene = new MyScene(this.engine, 10, 10);
            this.engine.setScene(playScene);
        }

        if (inputReceived(this.button5x8.getPos(), this.button5x8.getSize())){
            MyScene playScene = new MyScene(this.engine, 5, 8);
            this.engine.setScene(playScene);
        }

        if (inputReceived(this.button8x10.getPos(), this.button8x10.getSize())){
            MyScene playScene = new MyScene(this.engine, 8, 10);
            this.engine.setScene(playScene);
        }

        if (inputReceived(this.button5x10.getPos(), this.button5x10.getSize())){
            MyScene playScene = new MyScene(this.engine, 5, 10);
            this.engine.setScene(playScene);
        }

        if (inputReceived(this.backButton.getPos(), this.backButton.getSize())){
            this.engine.popScene();
        }
    }
}
