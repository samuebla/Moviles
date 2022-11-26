package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

//Ads
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
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
            this.engine.getGraphics().newFont("Calibri", "assets/CalibriRegular.ttf", 0, 25);
            this.engine.getGraphics().newFont("Cooper", "assets/CooperBlackRegular.ttf", 0, 50);
            this.engine.getGraphics().newFont("CalibriSmall", "assets/CalibriRegular.ttf", 0, 22);
            this.engine.getGraphics().newFont("CooperBold", "assets/CalibriRegular.ttf", 1, 40);
            this.engine.getGraphics().newFont("CalibriBold", "assets/CalibriRegular.ttf", 1, 20);

            this.engine.getGraphics().newImage("Board", "assets/board.png");
            this.engine.getGraphics().newImage("GiveUp", "assets/giveUpButton.png");
            this.engine.getGraphics().newImage("Back", "assets/backButton.png");
            this.engine.getGraphics().newImage("Check", "assets/checkButton.png");
            this.engine.getGraphics().newImage("PlayButton", "assets/playButton.png");
            this.engine.getGraphics().newImage("Coin", "assets/coin.png");
            this.engine.getGraphics().newImage("Heart", "assets/heart.png");

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        this.fastPlay = new Button(this.engine.getWidth() / 2 - (engine.getWidth() / 6), this.engine.getHeight() / 5, engine.getWidth() / 3, engine.getHeight() / 4.8);

        this.historyMode = new Button(this.engine.getWidth() / 2 - (engine.getWidth() / 6), this.engine.getHeight() / 2, engine.getWidth() / 3, engine.getHeight() / 4.8);





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

    @Override
    public void update(double deltaTime) {
        //Para los eventos
        if (this.engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput();
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
    public void handleInput() {
        //Si pulsas el boton...
        if (inputReceived(this.fastPlay.getPos(), this.fastPlay.getSize())) {
            //Te lleva a la pantalla de seleccion
            LevelSelection levelScene = new LevelSelection(this.engine);
            this.engine.setScene(levelScene);
        }
        if (inputReceived(this.historyMode.getPos(), this.historyMode.getSize())) {
            //Te lleva a la pantalla de seleccion
            HistoryModeMenu historyMode = new HistoryModeMenu(this.engine);
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