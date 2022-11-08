package com.example.engineandroid;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.lib.IAudio;
import com.example.lib.ISound;

import java.util.HashMap;

public class AudioAndroid implements IAudio {
    private static final String PATH = "";
    private HashMap<String,SoundApp> sounds;
    private SoundPool soundPool;

    //Para sonidos de mas de 1 mega (Para el background)
    private MediaPlayer mediaPlayer;

    public AudioAndroid(){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        this.soundPool = new SoundPool.Builder().
                setMaxStreams(10).
                setAudioAttributes(audioAttributes)
                .build();
    }

    @Override
    public ISound newSound(String audioName) {
        return new SoundApp(this.soundPool, PATH + audioName);
    }

    @Override
    public void playSound(String audioName) {
        SoundApp sound = new SoundApp(this.soundPool, PATH + audioName);
        sound.play();
    }
}
