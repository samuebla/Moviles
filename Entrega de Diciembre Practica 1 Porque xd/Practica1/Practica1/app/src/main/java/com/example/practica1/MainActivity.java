package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.logica.MainMenuScene;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EngineApp engine;

    private SurfaceView renderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = new SurfaceView(this);
        setContentView(this.renderView);

        //Escondemos la barra horizontal con los botones de android
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.engine = new EngineApp(this.renderView);

        //Escena principal la cual tambien carga los recursos del juego
        MainMenuScene mainMenuScene = new MainMenuScene();
        this.engine.getSceneMngr().pushScene(mainMenuScene);
        this.engine.setResourceScene(mainMenuScene);

        //Run del engine
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