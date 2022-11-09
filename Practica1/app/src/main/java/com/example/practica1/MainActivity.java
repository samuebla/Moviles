package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.SceneMngrAndroid;
import com.example.logica.MainMenuScene;
import com.example.logica.MyScene;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.SurfaceView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private EngineApp engine;

    private SurfaceView renderView;

    private AssetManager assetManager;

    private SceneMngrAndroid sceneMngr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = new SurfaceView(this);
        setContentView(this.renderView);

        getSupportActionBar().hide();
        this.engine = new EngineApp(this.renderView);

        this.assetManager = this.getBaseContext().getAssets();

        this.engine.setAssetsContext(this.assetManager);

        this.sceneMngr = new SceneMngrAndroid();

        this.engine.setSceneMngr(this.sceneMngr);

        //Inicializamos las fuentes y cargamos las que queramos
//        Font_Android[] fonts = new Font_Android[5];

//        try{
//            fonts[0] = new Font_Android("font/cooperblackregular.ttf",0,40, this.assetManager);
//            fonts[1] = new Font_Android("font/calibriregular.ttf",0,40, this.assetManager);
//            fonts[2] = new Font_Android("Assets\\CalibriRegular.ttf",1,18,this.assetManager);
//            fonts[3] = new Font_Android("Assets\\CooperBlackRegular.ttf",1,40,this.assetManager);
//            fonts[4] = new Font_Android("Assets\\CalibriRegular.ttf",1,20,this.assetManager);
//        }catch (Exception e){
//            System.err.println(e.getMessage());
//        }
//
//        String[] keys = new String[]{"Cooper","Calibri","CalibriSmall","CooperBold","CalibriBold"};
//
//        //Carga de Imagenes
//        ImageAndroid[] images = new ImageAndroid[2];
//
//        try{
//            images[0] = new ImageAndroid(this.assetManager, "arrow.png");
//            images[1] = new ImageAndroid(this.assetManager, "lupa.png");
//        }catch (Exception e){
//            System.err.println(e.getMessage());
//        }
//
//        String[] keysImages = new String[]{"Flecha","Lupa"};

//        AudioAndroid[] sounds = new AudioAndroid[2];
//
//        try {
////            sounds[0] = new AudioAndroid().newSound("Assets\\WiiBackgroundMusic.wav");
////            sounds[1] = new AudioAndroid().newSound("Assets\\wiiClickSound.wav");
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//
//        String[] keysSound = new String[]{"background", "effect"};



        engine.resume();
        MainMenuScene mainMenuScene = new MainMenuScene(this.engine);
        this.engine.setScene(mainMenuScene);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.engine.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.engine.pause();
    }

}