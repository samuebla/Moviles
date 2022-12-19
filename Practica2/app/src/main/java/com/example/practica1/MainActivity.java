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
import android.content.Intent;
import android.content.res.AssetManager;
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

public class MainActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WorkManager.getInstance(this).cancelAllWork();

        setContentView(R.layout.activity_main);

        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = findViewById(R.id.surfaceView);

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


        this.engine = new EngineApp(this.renderView, this.screenLayout, this);


        mainMenuScene = new MainMenuScene(this.engine, this.adContainerView, this.getBaseContext());
        this.engine.getSceneMngr().pushScene(mainMenuScene);
        this.engine.setPrimaryScene(mainMenuScene);
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
}