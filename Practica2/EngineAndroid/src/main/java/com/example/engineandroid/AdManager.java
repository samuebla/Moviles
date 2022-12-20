package com.example.engineandroid;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.concurrent.atomic.AtomicReference;

//Encargado de manejar los anuncios, mandar intents y crear el canal necesario para mandar notificaciones
public class AdManager {
    //Referencia a la actividad para poder crear los distintos canales, hacer intents, etc.
    private final Activity mainActivity;
    private final String TAG = "MainActivity";
    private RewardedAd mRewardedAd;

    //Id del canal de notificaciones
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
        CharSequence name = "channel";
        String description = "NotificationChannel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = this.mainActivity.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public String getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    //Manda un intent con formato de url con el mensaje especificado
    public void sendIntent(String url, String message) {
        Uri builtURI = Uri.parse(url).buildUpon()
                .appendQueryParameter("text", message)
                .build(); //Genera la URl https://twitter.com/intent/tweet?text=message
        Intent intent = new Intent(Intent.ACTION_VIEW, builtURI);
        this.mainActivity.startActivity(intent); // inicializa el intent
    }

    //Aumenta la variable introducida por amountToEarn si el usuario ve el anuncio hasta el final.
    public void showRewardedAd(AtomicReference<Integer> reward, Integer amountToEarn) {
        //Cambiamos a UiThread porque sino nopodemos llamar a rewardedAd
        mainActivity.runOnUiThread(() -> {
            if (mRewardedAd != null) {
                mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Creamos otro anuncio para que el usuario pueda ver varios anuncios
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
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
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
                //Mostramos el anuncio y aumentamos la variable si el usuario lo ve hasta el final
                mRewardedAd.show(mainActivity, rewardItem -> reward.set(reward.get() + amountToEarn));
            }
        });
    }
}
