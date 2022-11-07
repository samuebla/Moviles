package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.Font_Android;
import com.example.engineandroid.SceneMngrAndroid;
import com.example.logica.MainMenuScene;
import com.example.logica.MyScene;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.SurfaceView;

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


        this.engine = new EngineApp(this.renderView);

        this.sceneMngr = new SceneMngrAndroid();

        this.engine.setSceneMngr(this.sceneMngr);

        //Inicializamos las fuentes y cargamos las que queramos
        Font_Android[] fonts = new Font_Android[2];

        this.assetManager = this.getBaseContext().getAssets();

        try{
            fonts[0] = new Font_Android("cooperblackregular.ttf",0,40, this.assetManager);
            fonts[1] = new Font_Android("calibriregular.ttf",0,40, this.assetManager);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }

        String[] keys = new String[]{"Cooper","Calibri"};

        //AAAAAAAAAAA Cambiar la escena a la del menu principal
//        MyScene scene = new MyScene(this.engine,10,10, fonts, keys);
//        engine.setSceneManager(scene);

        MainMenuScene mainMenuScene = new MainMenuScene(this.engine, fonts, keys);

        this.engine.setScene(mainMenuScene);

        engine.resume();
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