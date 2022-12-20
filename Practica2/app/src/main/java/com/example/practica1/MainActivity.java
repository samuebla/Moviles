package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Engine
    private EngineApp engine;
    private SurfaceView renderView;

    //MainMenuScene, escena principal del engine, donde se cargan todos los recursos del juego
    MainMenuScene mainMenuScene;

    //AdView para el banner
    private AdView mAdView;

    //Sensores
    private SensorManager sensorManager;
    private Sensor sensor;
    int numShakes = 0;

    //Inicializacion de la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cancelamos todas las notificaciones pendientes para no andar mandando notificaciones al jugador mientras esta jugando
        WorkManager.getInstance(this).cancelAllWork();

        //Nuestro contentView que queremos utilizar es el layout guardado como activity_main.xml en la carpeta layout
        setContentView(R.layout.activity_main);

        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = findViewById(R.id.surfaceView);

        //AdView para el banner, presente en el xml
        mAdView = findViewById(R.id.adView);

        //Creamos el banner para que se vaya cargando y mostrando
        AdRequest adRequest = new AdRequest.Builder().build();

        //Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                //Add Initialization ----------------------------------------------

                mAdView.loadAd(adRequest);

                mAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdClicked() {
                        // Code to be executed when the user clicks on an ad.
                    }

                    @Override
                    public void onAdClosed() {
                        // Code to be executed when the user is about to return
                        // to the app after tapping on an ad.
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        // Code to be executed when an ad request fails.
                    }

                    @Override
                    public void onAdImpression() {
                        // Code to be executed when an impression is recorded
                        // for an ad.
                    }

                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                        super.onAdLoaded();
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when an ad opens an overlay that
                        // covers the screen.
                    }
                });
            }
        });

        //Escondemos la barra de acciones del movil
        Objects.requireNonNull(getSupportActionBar()).hide();


        //Inicializacion del engine
        this.engine = new EngineApp(this.renderView, this);

        //SENSOR
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        //registramos el listener
        sensorManager .registerListener( this, sensor , SensorManager.SENSOR_DELAY_NORMAL);

        //Cargamos la escena principal
        mainMenuScene = new MainMenuScene(getBaseContext());
        this.engine.getSceneMngr().pushScene(mainMenuScene);

        //Asigna la escena para que se carguen los assets a partir de ella, de esta manera las distintas componentes del engine ya han podido ser inicializadas correctamente y da tiempo a que no haya partes que sean null
        this.engine.setPrimaryScene(mainMenuScene);

        //Iniciamos la hebra del engine
        this.engine.resume();
    }

    //Crea una peticion al WorkManager para que haga una notificacion despues de 30 segundos
    private void createWorkRequest() {
        HashMap<String, Object> dataValues = new HashMap<>();
        dataValues.put("channelId", this.engine.getAdManager().getCHANNEL_ID());    //Id del canal, importante que este canal este inicializado desde el AdManager
        dataValues.put("smallIcon", androidx.constraintlayout.widget.R.drawable.notification_template_icon_low_bg); //Icono de la notificacion
        dataValues.put("contentTitle", "Nonogram"); //Titulo de la notificacion
        dataValues.put("contentText", "Llevas mucho tiempo sin jugar... Te echamos de menos :(");   //Texto de la notificacion
        dataValues.put("notificationId", 437);  //Id unico de la notificacion, es 437 por poner algo, lo unico importante es que no se repita
        Data inputData = new Data.Builder().putAll(dataValues).build(); //Datos que luego se pasan al Worker

        //Creamos el worker
        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(IntentWorkRequest.class)
                        //Configuracion, podemos cambiar 30 para especificar los segundos tras los que se hara la accion
                        .setInitialDelay(30, TimeUnit.SECONDS)
                        .setInputData(inputData)
                        .build();

        //Añadimos el worker al WorkManager de la actividad
        WorkManager.getInstance(this).enqueue(uploadWorkRequest);
    }

    //Llamado al final de la actividad, no confiar mucho en el preferiblemente, mejor usar onStop()
    @Override
    protected void onDestroy() {
        mainMenuScene.saveDataHistoryMode(getBaseContext());
        createWorkRequest();
        super.onDestroy();
    }

    //Llamado al final de la actividad, para la hebra del engine
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        mainMenuScene.saveDataHistoryMode(getBaseContext());
        engine.onStop();
        super.onSaveInstanceState(outState);
    }

    //Llamado al retomar la actividad, reinicia la hebra del engine
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        this.engine.resume();
    }

    //Llamado al interrumpirse la actividad
    @Override
    protected void onStop() {
        super.onStop();
        createWorkRequest();
    }

    //Llamado al pausarse la actividad
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        this.engine.pause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //Tienes que girar sobre si mismo con el movil agarrado para que le de una pista
        //Si lo giras a la izquierda...

        //1/-1 Es intacto, si giras 180º es 0.
        //Si haces medio giro...
        if (numShakes == 0 && event.values[2] > -0.2 && event.values[2] < 0.2) {
            //Lo registramos...
            numShakes = 1;
        }
        //Si haces el giro completo...
        if (numShakes == 1 && ((event.values[2] < -0.8 && event.values[2] > -1) || (event.values[2] > 0.8 && event.values[2] < 1))) {
            //Reseteamos para que lo vuelva a hacer
            numShakes = 0;
            //Y te regalamos 10 monedas
            mainMenuScene.addCoins(10);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}