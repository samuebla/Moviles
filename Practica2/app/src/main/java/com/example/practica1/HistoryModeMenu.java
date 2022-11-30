package com.example.practica1;

import android.content.Context;
import android.util.Log;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class HistoryModeMenu implements Scene {

    private Button themeButtonMode;
    private Button dificultyButtonMode;

    private Button backButton;

    AtomicReference<Integer> coins;
    private Integer coinSize;
    int[] unlockedThemedLevels;

    private AtomicReference<Integer>[] progress;

    private EngineApp engine;

    public HistoryModeMenu(EngineApp engineAux, AtomicReference<Integer> coinsAux, AtomicReference<Integer>[] progressAux){

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
//        loadCoinsFromFile();
        //Botones selectores del nivel
        this.themeButtonMode = new Button(engine.getWidth()/4  - engine.getWidth()/8, engine.getHeight()/2, engine.getWidth()/4, engine.getHeight()/6);
        this.dificultyButtonMode = new Button(engine.getWidth()*3/4 - engine.getWidth()/8, engine.getHeight()/2, engine.getWidth()/4, engine.getHeight()/6);
        this.backButton = new Button(10 + engine.getWidth()/44, 30, engine.getWidth()/10, engine.getHeight()/15);
    }

    public void update(double deltaTime){
        //Para los eventos...
        if(engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput();
            engine.getEventMngr().sendEvent(EventHandler.EventType.NONE);
        }
    }

    public void render(){
        //ThemeMode
        this.engine.drawImage((int)this.themeButtonMode.getPos().getX(), (int)this.themeButtonMode.getPos().getY(),(int)this.themeButtonMode.getSize().getX(),(int)this.themeButtonMode.getSize().getY(),"PlayButton");
        this.engine.drawText("Temático", (int)(themeButtonMode.getPos().getX() + themeButtonMode.getSize().getX()/2), (int)(themeButtonMode.getPos().getY() - themeButtonMode.getSize().getY()*0.5), "Black","Amor", 0);

        //DificultyMode
        this.engine.drawImage((int)this.dificultyButtonMode.getPos().getX(), (int)this.dificultyButtonMode.getPos().getY(),(int)this.dificultyButtonMode.getSize().getX(),(int)this.dificultyButtonMode.getSize().getY(),"PlayButton");
        this.engine.drawText("Dificultad", (int)(dificultyButtonMode.getPos().getX() + dificultyButtonMode.getSize().getX()/2), (int)(dificultyButtonMode.getPos().getY() - dificultyButtonMode.getSize().getY()*0.5), "Black","Amor", 0);

        //Back Button
        this.engine.drawImage((int)backButton.getPos().getX(), (int)backButton.getPos().getY(),(int)(backButton.getSize().getX()),(int)(backButton.getSize().getY()), "Back");

        //Texto indicativo
        this.engine.drawText("Selecciona el modo de Juego", (int)(engine.getWidth()/2), (int)(engine.getHeight()/5.4), "Black", "Amor", 0);

        //Moneda
        //MONEDAS
        this.engine.drawText(Integer.toString(coins.get()), engine.getWidth() - coinSize-10, (int)engine.getHeight()/15, "Black", "CooperBold", 1);
        this.engine.drawImage(engine.getWidth()-coinSize -10, (int)engine.getHeight()/72,coinSize,coinSize,"Coin");
    }

    public void handleInput(){
        //ThemeMode
        if (inputReceived(this.themeButtonMode.getPos(), this.themeButtonMode.getSize())){
            ThemeModeMenu playScene = new ThemeModeMenu(this.engine, this.coins, this.progress);
            this.engine.setScene(playScene);
        }

        //DificultyMode
        if (inputReceived(this.dificultyButtonMode.getPos(), this.dificultyButtonMode.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 8, 8);
            this.engine.setScene(playScene);
        }

        //Back button
        if (inputReceived(this.backButton.getPos(), this.backButton.getSize())){
            this.engine.popScene();
        }
    }

//    private void loadCoinsFromFile() {
//        try {
//            //Carga de archivo
//            String receiveString = "";
//            try {//Comprobar si existe en el almacenamiento interno
//                FileInputStream fis = this.engine.getContext().openFileInput("saveData");
//                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                while (bufferedReader.ready()) {
//                    receiveString += bufferedReader.readLine();
//                }
//                inputStreamReader.close();
//            } catch (FileNotFoundException e) { //Si no existe, crea un nuevo archivo en almacenamiento interno como copia desde assets
//                e.printStackTrace();
//                InputStreamReader inputStreamReader = new InputStreamReader(this.engine.getContext().getAssets().open("files/" + "saveData"));
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                while (bufferedReader.ready()) {
//                    receiveString += bufferedReader.readLine();
//                }
//
//                inputStreamReader.close();
//                //Copia del fichero
//                FileOutputStream fos = this.engine.getContext().openFileOutput("saveData", Context.MODE_PRIVATE);
//                fos.write(receiveString.getBytes());
//                fos.close();
//            }
//            //Carga el numero de monedas
//            String[] fileRead;
//            fileRead = receiveString.split(" ");
//            //Monedas
//            coins = Integer.parseInt(fileRead[0]);
//            //Niveles
//            for(int i = 1; i < fileRead.length;i++)
//                unlockedThemedLevels[i-1] = Integer.parseInt(fileRead[i]);
//
//        }
//        catch (
//                FileNotFoundException e) {
//            Log.e("Error", "File not found: " + e.toString());
//        } catch (
//                IOException e) {
//            Log.e("Reading Error", "Can not read file: " + e.toString());
//        }
//
//    }

    //Y aqui el guardado, recomiendo que este metodo lo pongamos aqui y podamos acceder a el desde todas las escenas para
    //que cada desbloqueo y cada transaccion de monedas se guarde al instante y no se tenga que salir
    //Tambien habria que hacer un getter en esta clase para saber cuantas monedas y niveles tienes
//    public void saveDataHistoryMode(int coins_, int idTheme, int unlockedLevels){   //Idtheme siempre debe ser desde 1
//        try {
//            FileOutputStream fos = this.engine.getContext().openFileOutput("saveData", Context.MODE_PRIVATE);
//            String writer = "";
//            //Monedas
//            if(coins_ >= 0)
//                writer += coins_ + " \n";
//            else
//                writer += coins + " \n";
//
//            //Nivel desbloqueado
//            if(idTheme > 0){
//                unlockedThemedLevels[idTheme] = unlockedLevels;
//            }
//            //Escribimos los niveles
//            for(int i = 1; i < unlockedThemedLevels.length;i++)
//                writer += unlockedThemedLevels[i];
//
//            fos.write(writer.getBytes(StandardCharsets.UTF_8));
//            fos.close();
//        } catch (FileNotFoundException e) {
//            Log.e("Error", "File not found: " + e.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
