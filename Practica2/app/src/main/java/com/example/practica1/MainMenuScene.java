package com.example.practica1;

import com.example.engineandroid.AdManager;
import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.RenderAndroid;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

//Ads
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;


public class MainMenuScene implements Scene {

    //TODO A partir de ahora tenemos una escala de 1000x1000, asi que no usamos mas engine.getWidth ni engine.getHeight
    int scaleWidth = 1000;
    int scaleHeight = 1000;

    private final EngineApp engine;
    private InputButton fastPlay;
    private InputButton historyMode;

    //Player Data
    private AtomicReference<Integer> coins;
    private AtomicReference<Integer>[] progress;
    private AtomicReference<Integer>[] palettes;


    Context baseContext;

    public MainMenuScene(EngineApp engineAux, Context contextAux) {
        this.engine = engineAux;
        this.baseContext = contextAux;

        loadFromFile();
        System.out.print("Save data loaded: ");
        System.out.print(this.coins);
        for (AtomicReference<Integer> integerAtomicReference : this.progress) {
            System.out.print(integerAtomicReference);
        }
    }


    //TODO AAA QUITAR ESTO KE MAL KE MAL
    //Actualmente Pos y Size se devuelve en unidades de 0 a 1000 pero el getScaledCoords esta en cordenadas reales por eso hago la conversion
    @Override
    public boolean inputReceived(Vector2D pos, Vector2D size) {
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), this.engine.getInput().getScaledCoords().getY());

        return (coords.getX()*scaleWidth/engine.getGraphics().getWidth() >= pos.getX()  && coords.getX()*scaleWidth/engine.getGraphics().getWidth() <= pos.getX() + size.getX() &&
                coords.getY()*scaleHeight/engine.getGraphics().getHeight() >= pos.getY() && coords.getY()*scaleHeight/engine.getGraphics().getHeight() <= pos.getY() + size.getY());
    }

    @Override
    public void init() {

        this.fastPlay = new InputButton(scaleWidth / 2 - (scaleWidth/ 4), scaleHeight / 5, scaleWidth / 2, scaleHeight / 4.8);

        this.historyMode = new InputButton(scaleWidth / 2 - (scaleWidth / 4), scaleHeight / 2, scaleWidth/ 2, scaleHeight/ 4.8);
    }

    @Override
    public void loadResources(EngineApp engineAux) {
        try {
            //La constructora del menu solo se llama una vez
            //Cargamos el fondo y lo playeamos
            engineAux.getAudio().loadMusic( "assets/WiiBackgroundMusic.wav");
            engineAux.getAudio().playSound("background", 0);
            engineAux.getAudio().newSound("effect", "assets/wiiClickSound.wav");

            engineAux.getGraphics().newFont("Amor", "assets/AmorRegular.ttf", 0);
            engineAux.getGraphics().newFont("Calibri", "assets/CalibriRegular.ttf", 0);
            engineAux.getGraphics().newFont("Cooper", "assets/CooperBlackRegular.ttf", 0);
            engineAux.getGraphics().newFont("CooperBold", "assets/CalibriRegular.ttf", 1);
            engineAux.getGraphics().newFont("CalibriBold", "assets/CalibriRegular.ttf", 1);

            engineAux.getGraphics().newImage("Board", "assets/board.png");
            engineAux.getGraphics().newImage("GiveUp", "assets/giveUpButton.png");
            engineAux.getGraphics().newImage("Back", "assets/backButton.png");
            engineAux.getGraphics().newImage("Check", "assets/checkButton.png");
            engineAux.getGraphics().newImage("Coin", "assets/coin.png");
            engineAux.getGraphics().newImage("Heart", "assets/heart.png");
            engineAux.getGraphics().newImage("Square", "assets/square.png");

            //NIVELES
            engineAux.getGraphics().newImage("PartyUnlocked", "assets/party_unlocked.png");
            engineAux.getGraphics().newImage("AnimalsUnlocked", "assets/animals_unlocked.png");
            engineAux.getGraphics().newImage("GeometryUnlocked", "assets/geometry_unlocked.png");
            engineAux.getGraphics().newImage("AlphabetUnlocked", "assets/alphabet_unlocked.png");

            engineAux.getGraphics().newImage("QuickPlay", "assets/quick_play.png");
            engineAux.getGraphics().newImage("HistoryPlay", "assets/history_play.png");
            engineAux.getGraphics().newImage("ThemePlay", "assets/theme_play.png");
            engineAux.getGraphics().newImage("ChallengePlay", "assets/challenge_play.png");
            engineAux.getGraphics().newImage("GeometryLevel", "assets/geometry_level.png");
            engineAux.getGraphics().newImage("AnimalsLevel", "assets/animals_level.png");
            engineAux.getGraphics().newImage("PartyLevel", "assets/party_level.png");
            engineAux.getGraphics().newImage("AlphabetLevel", "assets/alphabet_level.png");

            engineAux.getGraphics().newImage("GeometryPlay", "assets/geometry_play.png");
            engineAux.getGraphics().newImage("AnimalsPlay", "assets/animals_play.png");
            engineAux.getGraphics().newImage("PartyPlay", "assets/party_play.png");
            engineAux.getGraphics().newImage("AlphabetPlay", "assets/alphabet_play.png");
            //-------------------------------

            //Paletas de colores
            engineAux.getGraphics().newImage("YellowPalette", "assets/yellow_palette.png");
            engineAux.getGraphics().newImage("PinkPalette", "assets/pink_palette.png");
            engineAux.getGraphics().newImage("BluePalette", "assets/blue_palette.png");
            engineAux.getGraphics().newImage("WhitePalette", "assets/white_palette.png");

            engineAux.getGraphics().newImage("HeartAD", "assets/extra_heart.png");
            engineAux.getGraphics().newImage("CoinsCost", "assets/coste_monedas.png");
            engineAux.getGraphics().newImage("TwitterIcon", "assets/twitter_icon.png");

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    //Con este metodo el sensor te da newCoins monedas cada vez que des una vuelta sobre ti mismo
    public void addCoins(int newCoins){
        coins.set(coins.get()+newCoins);
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
            this.coins = new AtomicReference<>(Integer.parseInt(fileRead[0]));

            this.progress = new AtomicReference[4];
            this.palettes = new AtomicReference[4];

            for (int i = 0; i < this.progress.length; ++i) {
                this.progress[i] = new AtomicReference<>(Integer.parseInt(fileRead[i + 1]));
            }

            //Paletas de colores
            for (int i = 0; i < this.palettes.length; ++i) {
                this.palettes[i] = new AtomicReference<>();
                this.palettes[i].set(Integer.parseInt(fileRead[this.progress.length + 1 + i]));
            }

        } catch (
                FileNotFoundException e) {
            Log.e("Error", "Save data file not found: " + e);
        } catch (
                IOException e) {
            Log.e("Reading Error", "Can not read save data file: " + e);
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

            for (AtomicReference<Integer> integerAtomicReference : this.progress) {
                writer += integerAtomicReference.get();
                writer += " ";
            }
            writer += "\n";

            for (AtomicReference<Integer> palette : this.palettes) {
                writer += palette.get();
                writer += " ";
            }

            fos.write(writer.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("Error", "File not found: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(double deltaTime, AdManager adManager) {
        //Para los eventos
        if (this.engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput(engine.getEventMngr().getEventType(), adManager);
            this.engine.getEventMngr().sendEvent(EventHandler.EventType.NONE);
        }
    }

    @Override
    public void render(RenderAndroid render) {


        //Titulo
        render.drawText("NONOGRAMAS", (int) (scaleWidth / 2.0), (int) (scaleHeight / 10.8), "Black", "Cooper", 0,(scaleWidth/14));

        //Botones
        render.drawImage((int) this.fastPlay.getPos().getX(), (int) (fastPlay.getPos().getY()), (int) (this.fastPlay.getSize().getX()), (int) (this.fastPlay.getSize().getY()), "QuickPlay");
        render.drawImage((int) this.historyMode.getPos().getX(), (int) (historyMode.getPos().getY()), (int) (this.historyMode.getSize().getX()), (int) (this.historyMode.getSize().getY()), "HistoryPlay");

    }

    @Override
    public void handleInput(EventHandler.EventType type, AdManager adManager) {
        //Si pulsas el boton...
        if (inputReceived(this.fastPlay.getPos(), this.fastPlay.getSize())) {
            //Te lleva a la pantalla de seleccion
            LevelSelection levelScene = new LevelSelection(this.engine);
            this.engine.getSceneMngr().pushScene(levelScene);
        }
        if (inputReceived(this.historyMode.getPos(), this.historyMode.getSize())) {
            //Te lleva a la pantalla de seleccion
            HistoryModeMenu historyMode = new HistoryModeMenu(this.engine, this.coins, this.progress,this.palettes);
            this.engine.getSceneMngr().pushScene(historyMode);
        }
    }

    //Se llama cuando la escena posterior se elimina y se vuelve aqui, por si hay que actualizar algo
    @Override
    public void onResume() {
        init();
    }
}