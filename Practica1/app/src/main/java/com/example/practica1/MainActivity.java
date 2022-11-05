package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.Font_Android;
import com.example.logica.MyScene;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private EngineApp engine;

    private SurfaceView renderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = new SurfaceView(this);
        setContentView(this.renderView);


        this.engine = new EngineApp(this.renderView);

        //Inicializamos las fuentes y cargamos las que queramos
        Font_Android[] fonts = new Font_Android[2];

        fonts[0] = new Font_Android("Assets\\CooperBlackRegular.ttf",0,40, this.getAssets());
        fonts[1] = new Font_Android("Assets\\CalibriRegular.ttf",0,40, this.getAssets());

        String[] keys = new String[]{"Cooper","Calibri"};

        MyScene scene = new MyScene(this.engine,10,10, fonts, keys);
        engine.setScene(scene);
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