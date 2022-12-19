package com.example.engineandroid;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

import java.util.concurrent.atomic.AtomicReference;

public class AdManager {
    private Activity mainActivity;
    private final String TAG = "MainActivity";
    private RewardedAd mRewardedAd;


    private final String CHANNEL_ID = "NonogramChannelId";




    public AdManager(Activity mainActivity) {
        this.mainActivity = mainActivity;

        //Reward ad creation
        AdRequest adRequest2 = new AdRequest.Builder().build();
        RewardedAd.load(this.mainActivity, "ca-app-pub-3940256099942544/5224354917",
                adRequest2, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O) {
            CharSequence name = "channel";
            String description = "NotificationChannel";
            int importance = NotificationManager. IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID , name, importance) ;
            channel.setDescription(description) ;
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.mainActivity.getSystemService(NotificationManager. class);
            notificationManager.createNotificationChannel(channel);
        }



//
//        synchronized(notificationManager){
//            try {
//                notificationManager.wait(20000);
//                // notificationId is a unique int for each notification that you must define
//                notificationManager.notify(347, notificationBuilder.build());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public String getCHANNEL_ID(){
        return CHANNEL_ID;
    }

    public void sendIntent(int sendType, String url, String message){
        Uri builtURI = Uri. parse(url ).buildUpon()
                .appendQueryParameter( "text", message)
                .build() ; //Genera la URl https://twitter.com/intent/tweet?text=message
        Intent intent = new Intent(Intent. ACTION_VIEW, builtURI);
        this.mainActivity.startActivity(intent) ; // inicializa el intent
    }

    //Aumenta la variable introducida por amountToEarn si el usuario ve el anuncio hasta el final.
    public void showRewardedAd(AtomicReference<Integer> reward, Integer amountToEarn) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRewardedAd != null) {
                    mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Log.d(TAG, "Ad dismissed fullscreen content.");
                            AdRequest adRequest2 = new AdRequest.Builder().build();
                            RewardedAd.load(mainActivity, "ca-app-pub-3940256099942544/5224354917",
                                    adRequest2, new RewardedAdLoadCallback() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                            // Handle the error.
                                            Log.d(TAG, loadAdError.toString());
                                            mRewardedAd = null;
                                        }

                                        @Override
                                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                            mRewardedAd = rewardedAd;
                                            Log.d(TAG, "Ad was loaded.");
                                        }
                                    });
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when ad fails to show.
                            Log.e(TAG, "Ad failed to show fullscreen content.");
                            mRewardedAd = null;
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
                    mRewardedAd.show(mainActivity, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            reward.set(reward.get() + amountToEarn);

                            // Handle the reward.
                            // Log.d(TAG, "The user earned the reward.");
                            // int rewardAmount = rewardItem.getAmount();
                            // String rewardType = rewardItem.getType();
                            // }
                            // });
                            // } else {
                            // System.out.println("The rewarded ad wasn't ready yet.");
                            // Log.d(TAG, "The rewarded ad wasn't ready yet.");
                            // }
                            // }
                            // });
                        }
                    });
                }
            }
        });
    }
}
