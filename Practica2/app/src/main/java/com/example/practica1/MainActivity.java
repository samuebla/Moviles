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

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.LinearLayout;

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
    private final String CHANNEL_ID = "NonogramChannelId";
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder notificationBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.screenLayout = findViewById(R.id.linearLayout);

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


        if (savedInstanceState == null){
            this.engine = new EngineApp(this.renderView, this.screenLayout);
        }else{
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

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O) {
//            CharSequence name = getString(R.string.channel_name) ;
//            String description = getString(R.string.channel_description) ;
//            int importance = NotificationManager. IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID , name, importance) ;
//            channel.setDescription(description) ;
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager. class);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationBuilder = new NotificationCompat.Builder( this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle( "My notification" )
//                .setContentText( "Much longer text that cannot fit one line..." )
//                .setStyle( new NotificationCompat.BigTextStyle()
//                        .bigText( "Much longer text that cannot fit one line..." ))
//                .setPriority(NotificationCompat. PRIORITY_DEFAULT);
//
//        notificationManager = NotificationManagerCompat.from(this);

//        synchronized(notificationManager){
//            try {
//                notificationManager.wait(20000);
//                // notificationId is a unique int for each notification that you must define
//                notificationManager.notify(347, notificationBuilder.build());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        if(savedInstanceState == null){
            mainMenuScene = new MainMenuScene(this.engine, this.adContainerView, this.getBaseContext());
            this.engine.getSceneMngr().pushScene(mainMenuScene);
            this.engine.setPrimaryScene(mainMenuScene);
        }else{
            mainMenuScene = (MainMenuScene) savedInstanceState.getSerializable("mainMenuScene");
            this.engine.setPrimaryScene(mainMenuScene);
        }
        this.engine.resume();
    }

    @Override
    protected void onDestroy(){
        mainMenuScene.saveDataHistoryMode();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState (Bundle outState){
//        outState.putInt("lastScene", this.engine.onForcedClose());
        outState.putSerializable("mainMenuScene", this.mainMenuScene);
        outState.putSerializable("sceneManager", this.engine.getSceneMngr());
        super.onSaveInstanceState(outState);
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

    public void sendIntent(int sendType, String url, String message){
        Uri builtURI = Uri. parse(url ).buildUpon()
                .appendQueryParameter( "text", message)
                .build() ; //Genera la URl https://twitter.com/intent/tweet?text=Este%20es%20mi%20texto%20a%20tweettear
        Intent intent = new Intent(Intent. ACTION_VIEW, builtURI);
        this.startActivity(intent) ; // inicializa el intent
    }

    //Para comprobar la lista de aplicaciones que pueden abrir el intent
    public void checkResolver(){
        Intent share = new Intent(android.content.Intent. ACTION_SEND);
        share.setType( "image/jpeg" );
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