package com.example.practica1;

import com.example.engineandroid.EngineApp;
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

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
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

    private SceneMngrAndroid sceneMngr;

    private FrameLayout adContainerView;

    LinearLayout screenLayout;

    MainMenuScene mainMenuScene;

    AdView mAdView;
    private AtomicReference<RewardedAd> mRewardedAd;
    private AtomicReference<MainActivity> mainActivity;
    private final String TAG = "MainActivity";
    private Button rewardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });

        setContentView(R.layout.activity_main);
        this.screenLayout = findViewById(R.id.linearLayout);

        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = findViewById(R.id.surfaceView);

        this.rewardButton = findViewById(R.id.show_reward_button);

//        View screen = findViewById(R.id.constraint);


        getSupportActionBar().hide();
        this.engine = new EngineApp(this.renderView, this.screenLayout);

        this.assetManager = this.getBaseContext().getAssets();

        this.engine.setBaseContext(this.getBaseContext());

        this.engine.setAssetsContext(this.assetManager);

        this.sceneMngr = new SceneMngrAndroid();

        this.engine.setSceneMngr(this.sceneMngr);

        //Add Initialization ----------------------------------------------
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
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
                engine.updateSurfaceSize();
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
                engine.updateSurfaceSize();

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });

        mRewardedAd = new AtomicReference<RewardedAd>();
        this.mainActivity = new AtomicReference<MainActivity>();
        this.mainActivity.set(this);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest2, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        mRewardedAd.set(null);
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd.set(rewardedAd);
                        Log.d(TAG, "Ad was loaded.");
                    }
                });

        rewardButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mRewardedAd.get() != null)
                            showRewardedAd();
                    }
                });

        //---------------------------------------------------------------------

        //Intent example
        //sendIntent(0, "https://twitter.com/intent/tweet", "oh wow Prueba");

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O) {
//            CharSequence name = getString(R.string.channel_name) ;
//            String description = getString(R.string.channel_description) ;
//            int importance = NotificationManager. IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID , name, importance) ;
//            channel.setDescription(description) ;
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager. class);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.notification_icon)
//                .setContentTitle( "My notification" )
//                .setContentText( "Much longer text that cannot fit one line..." )
//                .setStyle( new NotificationCompat.BigTextStyle()
//                        .bigText( "Much longer text that cannot fit one line..." ))
//                .setPriority(NotificationCompat. PRIORITY_DEFAULT);





        mainMenuScene = new MainMenuScene(this.engine, this.adContainerView, this.getBaseContext(), this.rewardButton);
        this.engine.setScene(mainMenuScene);
        this.engine.resume();
    }

    @Override
    protected void onDestroy(){
        mainMenuScene.saveDataHistoryMode();
        super.onDestroy();
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

    public void showRewardedAd(){
        mRewardedAd.get().setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
                mRewardedAd.set(null);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                mRewardedAd.set(null);
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
            }
        });

        Activity activityContext = MainActivity.this;
        mRewardedAd.get().show(activityContext, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                // Handle the reward.
                Log.d("MAIN ACTIVITY", "The user earned the reward.");
                int rewardAmount = rewardItem.getAmount();
                String rewardType = rewardItem.getType();
            }
        });
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