package com.example.practica1;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.Scene;
import com.example.engineandroid.SceneMngrAndroid;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.WorkSource;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private EngineApp engine;

    private SurfaceView renderView;

    private AssetManager assetManager;

    private FrameLayout adContainerView;

    LinearLayout screenLayout;

    MainMenuScene mainMenuScene;

    private AdView mAdView;

    //    AdView mAdView;
    private AtomicReference<MainActivity> mainActivity;
    private Button rewardButton;

    //Sensores
    private SensorManager sensorManager;
    private Sensor sensor;

    int numShakes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WorkManager.getInstance(this).cancelAllWork();

        setContentView(R.layout.activity_main);
//        this.screenLayout = findViewById(R.id.linearLayout);

        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = findViewById(R.id.surfaceView);

//        this.rewardButton = findViewById(R.id.show_reward_button);

//        View screen = findViewById(R.id.constraint);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        //Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
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
                    public void onAdFailedToLoad(LoadAdError adError) {
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

        getSupportActionBar().hide();


        if (savedInstanceState == null) {
            this.engine = new EngineApp(this.renderView, this.screenLayout, this);
        } else {
            //Conseguimos el engine si se ha reiniciado la aplicacion
            this.engine = ((MainMenuScene) savedInstanceState.getSerializable("mainMenuScene")).getEngine();
            this.engine.restart(this.renderView, this.screenLayout);
        }

//        mRewardedAd = new AtomicReference<RewardedAd>();
//        this.mainActivity = new AtomicReference<MainActivity>();
//        this.mainActivity.set(this);
//        AdRequest adRequest2 = new AdRequest.Builder().build();
//        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
//                adRequest2, new RewardedAdLoadCallback() {
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error.
//                        Log.d(TAG, loadAdError.toString());
//                        mRewardedAd.set(null);
//                    }
//
//                    @Override
//                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
//                        mRewardedAd.set(rewardedAd);
//                        Log.d(TAG, "Ad was loaded.");
//                    }
//                });

//        rewardButton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(mRewardedAd.get() != null)
//                            showRewardedAd();
//                    }
//                });

        //---------------------------------------------------------------------

        //Intent example
//        sendIntent(0, "https://twitter.com/intent/tweet", "oh wow Prueba");

        //SENSOR
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        //registramos el listener
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        if (savedInstanceState == null) {
            mainMenuScene = new MainMenuScene(this.engine, this.adContainerView, this.getBaseContext());
            this.engine.getSceneMngr().pushScene(mainMenuScene);
            this.engine.setPrimaryScene(mainMenuScene);
        } else {
            mainMenuScene = (MainMenuScene) savedInstanceState.getSerializable("mainMenuScene");
            this.engine.setPrimaryScene(mainMenuScene);
        }
        this.engine.resume();
    }

    private void createWorkRequest(String title, String text) {
        HashMap<String, Object> dataValues = new HashMap<>();
        dataValues.put("channelId", this.engine.getAdManager().getCHANNEL_ID());
        dataValues.put("smallIcon", androidx.constraintlayout.widget.R.drawable.notification_template_icon_low_bg);
        dataValues.put("contentTitle", title);
        dataValues.put("contentText", text);
        dataValues.put("notificationId", 437);
        Data inputData = new Data.Builder().putAll(dataValues).build();

        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(IntentWorkRequest.class)
                        //Configuration
                        .setInitialDelay(30, TimeUnit.SECONDS)
                        .setInputData(inputData)
                        .build();

        WorkManager.getInstance(this).enqueue(uploadWorkRequest);
    }

    @Override
    protected void onDestroy() {
        mainMenuScene.saveDataHistoryMode();
        createWorkRequest("Nonogram", "Llevas mucho tiempo sin jugar... Te echamos de menos :(");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mainMenuScene.saveDataHistoryMode();
//        outState.putInt("lastScene", this.engine.onForcedClose());
        outState.putSerializable("mainMenuScene", this.mainMenuScene);
        outState.putSerializable("sceneManager", this.engine.getSceneMngr());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        this.engine.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        createWorkRequest("Nonogram", "Llevas mucho tiempo sin jugar... Te echamos de menos :(");
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        this.engine.pause();
    }

    //Para comprobar la lista de aplicaciones que pueden abrir el intent
    public void checkResolver() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");
//        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share , 0);
//        if (!resInfo.isEmpty()){
//            for (ResolveInfo info : resInfo) {
//                if (info.activityInfo .packageName .toLowerCase().contains(nameApp) ||
//                        info. activityInfo .name.toLowerCase().contains(nameApp)) {
//                    share.setPackage(info.activityInfo.packageName);
//                // add other info if necessary
//                }
//            }
//            context.startActivity(share) ;
//        }
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


//    private AdRequest getAdRequest() {
//        // Create an ad request. Check your logcat output for the hashed device ID
//        // to get test ads on a physical device, e.g.,
//        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
//        // device."
//
//        return request;
//    }

    // Determine the screen width (less decorations) to use for the ad width.
//    private AdSize getAdSize() {
//        WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
//        Rect bounds = windowMetrics.getBounds();
//
//        float adWidthPixels = adContainerView.getWidth();
//
//        // If the ad hasn't been laid out, default to the full screen width.
//        if (adWidthPixels == 0f) {
//            adWidthPixels = bounds.width();
//        }
//
//        float density = getResources().getDisplayMetrics().density;
//        int adWidth = (int) (adWidthPixels / density);
//
//        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
//    }

}