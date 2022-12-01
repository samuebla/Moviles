package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

//Ads
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainMenuScene implements Scene {

    private EngineApp engine;
    private Button fastPlay;
    private Button historyMode;

    //Player Data
    private AtomicReference<Integer> coins;
    private AtomicReference<Integer>[] progress;

    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
//    private AdView adView;

    private FrameLayout addContainerView;
    //    private AdSize adsize;
//    private AdRequest addRequest;
    Context baseContext;
    private boolean initialLayoutComplete = false;

    public MainMenuScene(EngineApp engineAux, FrameLayout addContainerViewAux, Context contextAux) {
        this.engine = engineAux;
        this.addContainerView = addContainerViewAux;
//        this.adsize = size;
//        this.addRequest = addRequestAux;
        this.baseContext = contextAux;
    }


    @Override
    public boolean inputReceived(Vector2D pos, Vector2D size) {
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), this.engine.getInput().getScaledCoords().getY());

        return (coords.getX() >= pos.getX() && coords.getX() <= pos.getX() + size.getX() &&
                coords.getY() >= pos.getY() && coords.getY() <= pos.getY() + size.getY());
    }

    @Override
    public void init() {
        try {
            //La constructora del menu solo se llama una vez
            //Cargamos el fondo y lo playeamos
            this.engine.getAudio().loadMusic("background", "assets/WiiBackgroundMusic.wav");
            this.engine.getAudio().playSound("background", 0);
            this.engine.getAudio().newSound("effect", "assets/wiiClickSound.wav");

            this.engine.getGraphics().newFont("Amor", "assets/AmorRegular.ttf", 0, 50);
            this.engine.getGraphics().newFont("Calibri", "assets/CalibriRegular.ttf", 0, 40);
            this.engine.getGraphics().newFont("Cooper", "assets/CooperBlackRegular.ttf", 0, 50);
            this.engine.getGraphics().newFont("CalibriSmall", "assets/CalibriRegular.ttf", 0, 30);
            this.engine.getGraphics().newFont("CooperBold", "assets/CalibriRegular.ttf", 1, 40);
            this.engine.getGraphics().newFont("CalibriBold", "assets/CalibriRegular.ttf", 1, 30);

            this.engine.getGraphics().newImage("Board", "assets/board.png");
            this.engine.getGraphics().newImage("GiveUp", "assets/giveUpButton.png");
            this.engine.getGraphics().newImage("Back", "assets/backButton.png");
            this.engine.getGraphics().newImage("Check", "assets/checkButton.png");
            this.engine.getGraphics().newImage("PlayButton", "assets/playButton.png");
            this.engine.getGraphics().newImage("Coin", "assets/coin.png");
            this.engine.getGraphics().newImage("Heart", "assets/heart.png");
            this.engine.getGraphics().newImage("Square", "assets/square.png");
            this.engine.getGraphics().newImage("Blocked", "assets/blockedLevel.png");
            this.engine.getGraphics().newImage("Unlocked", "assets/unlockedLevel.png");


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        this.fastPlay = new Button(this.engine.getWidth() / 2 - (engine.getWidth() / 6), this.engine.getHeight() / 5, engine.getWidth() / 3, engine.getHeight() / 4.8);

        this.historyMode = new Button(this.engine.getWidth() / 2 - (engine.getWidth() / 6), this.engine.getHeight() / 2, engine.getWidth() / 3, engine.getHeight() / 4.8);


        loadFromFile();
        System.out.print("Save data loaded: ");
        System.out.print(this.coins);
        for (int i = 0; i < this.progress.length; ++i) {
            System.out.print(this.progress[i]);
        }


        //Creacion anuncio
//        adView = new AdView(this.baseContext);
//        addContainerView.addView(adView);
//        // Since we're loading the banner based on the adContainerView size, we need
//        // to wait until this view is laid out before we can get the width.
//        addContainerView.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        if (!initialLayoutComplete) {
//                            initialLayoutComplete = true;
//                            loadBanner();
//                        }
//                    }
//                });
    }

    //Metodos de lectura y guardado
    public void loadFromFile() {
        try {
//            //Carga de archivo
            String receiveString = "";
            try {//Comprobar si existe en el almacenamiento interno
                FileInputStream fis = this.engine.getContext().openFileInput("saveData");
                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                while (bufferedReader.ready()) {
                    receiveString += bufferedReader.readLine();
                }
                inputStreamReader.close();
            } catch (FileNotFoundException e) { //Si no existe, crea un nuevo archivo en almacenamiento interno como copia desde assets
                e.printStackTrace();
            }
            InputStreamReader inputStreamReader = new InputStreamReader(this.engine.getContext().getAssets().open("files/saveData"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while (bufferedReader.ready()) {
                receiveString += bufferedReader.readLine();
            }

            inputStreamReader.close();
            //Copia del fichero
            FileOutputStream fos = this.engine.getContext().openFileOutput("saveData", Context.MODE_PRIVATE);
            fos.write(receiveString.getBytes());
            fos.close();
            //Carga el nivel desde el string "RAW" de lectura
            String[] fileRead;
            fileRead = receiveString.split(" ");
            this.coins = new AtomicReference<Integer>(Integer.parseInt(fileRead[0]));

            this.progress = new AtomicReference[4];

            for (int i = 0; i < this.progress.length; ++i) {
                this.progress[i] = new AtomicReference<Integer>(Integer.parseInt(fileRead[i + 1]));
            }
            //AAAAAAAAAAAAAAAAAAAAAAAAAA
            int patata = 0;

        } catch (
                FileNotFoundException e) {
            Log.e("Error", "Save data file not found: " + e.toString());
        } catch (
                IOException e) {
            Log.e("Reading Error", "Can not read save data file: " + e.toString());
        }
    }

    //Y aqui el guardado, recomiendo que este metodo lo pongamos aqui y podamos acceder a el desde todas las escenas para
    //que cada desbloqueo y cada transaccion de monedas se guarde al instante y no se tenga que salir
    //Tambien habria que hacer un getter en esta clase para saber cuantas monedas y niveles tienes
    public void saveDataHistoryMode(){   //Idtheme siempre debe ser desde 1
        try {
            FileOutputStream fos = this.engine.getContext().openFileOutput("saveData", Context.MODE_PRIVATE);
            String writer = "";
            //Monedas
            writer += coins.get() + " \n";

            for(int i = 0; i < this.progress.length; ++i){
                writer += this.progress[i].get();
                writer += " ";
            }

            fos.write(writer.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("Error", "File not found: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(double deltaTime) {
        //Para los eventos
        if (this.engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput(engine.getEventMngr().getEventType());
            this.engine.getEventMngr().sendEvent(EventHandler.EventType.NONE);
        }
    }

    @Override
    public void render() {
        //Titulo
        this.engine.drawText("NONOGRAMAS", (int) (this.engine.getWidth() / 2), (int) (this.engine.getHeight() / 10.8), "Black", "Cooper", 0);

        //Botones
        this.engine.drawImage((int) this.fastPlay.getPos().getX(), (int) (fastPlay.getPos().getY()), (int) (this.fastPlay.getSize().getX()), (int) (this.fastPlay.getSize().getY()), "PlayButton");
        this.engine.drawImage((int) this.historyMode.getPos().getX(), (int) (historyMode.getPos().getY()), (int) (this.historyMode.getSize().getX()), (int) (this.historyMode.getSize().getY()), "PlayButton");

    }

    @Override
    public void handleInput(EventHandler.EventType type) {
        //Si pulsas el boton...
        if (inputReceived(this.fastPlay.getPos(), this.fastPlay.getSize())) {
            //Te lleva a la pantalla de seleccion
            LevelSelection levelScene = new LevelSelection(this.engine);
            this.engine.setScene(levelScene);
        }
        if (inputReceived(this.historyMode.getPos(), this.historyMode.getSize())) {
            //Te lleva a la pantalla de seleccion
            HistoryModeMenu historyMode = new HistoryModeMenu(this.engine, this.coins, this.progress);
            this.engine.setScene(historyMode);
        }
    }

//    private void loadBanner() {
//        adView.setAdUnitId(AD_UNIT_ID);
//
//        AdSize adSize = this.adsize;
//        adView.setAdSize(adSize);
//
//        // Start loading the ad in the background.
//        adView.loadAd(this.addRequest);
//    }
}