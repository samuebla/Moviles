package com.example.engineandroid;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

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


    public AdManager(Activity mainActivity) {
        this.mainActivity = mainActivity;

        AdRequest adRequest2 = new AdRequest.Builder().build();
//        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
//                adRequest2, new RewardedAdLoadCallback() {
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error.
//                        Log.d(TAG, loadAdError.toString());
//                        mRewardedAd = null;
//                    }
//
//                    @Override
//                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
//                        mRewardedAd = rewardedAd;
//                        Log.d(TAG, "Ad was loaded.");
//                    }
//                });
    }

    public void showRewardedAd() {
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
                            mRewardedAd = null;
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
