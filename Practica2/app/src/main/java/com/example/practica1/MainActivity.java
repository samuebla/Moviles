package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.SceneMngrAndroid;

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

        engine.resume();
//        MainMenuScene mainMenuScene = new MainMenuScene(this.engine);
//        this.engine.setScene(mainMenuScene);
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