package com.example.engineandroid;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.lib.IAudio;
import com.example.lib.ISound;

import java.util.HashMap;
import java.util.Objects;

public class AudioAndroid implements IAudio {
    private static final String PATH = "";

    //Para sonidos cortos
    private final HashMap<String,SoundApp> sounds;
    private final SoundPool soundPool;

    //Para sonidos de mas de 1 mega (Para el background)
    private final MediaPlayer mediaPlayer;

    private final AssetManager assets;

    public AudioAndroid(AssetManager assets){
        //Inicializamos atributos
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        //SoundPool que reproduce los sonidos de efecto
        this.soundPool = new SoundPool.Builder().
                setMaxStreams(10).
                setAudioAttributes(audioAttributes)
                .build();

        this.assets = assets;

        //Media player para la musica de fondo
        this.mediaPlayer = new MediaPlayer();

        //Aqui guardamos los sonidos de efecto
        this.sounds = new HashMap<>();
    }

    @Override
    public ISound newSound(String audioName, String path) {
        //Guarda en los sounds y crea el nuevo sonido
        return this.sounds.put(audioName, new SoundApp(this.soundPool, PATH + path, this.assets));
    }

    @Override
    public void loadMusic(String audioName, String path){
        //Inicializamos la musica
        this.mediaPlayer.reset();
        String newFilePath = path.replaceAll("assets/", "");
        AssetFileDescriptor fileDescriptor;
        try{
            fileDescriptor = this.assets.openFd(newFilePath);
            this.mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            this.mediaPlayer.setVolume(1.0f, 1.0f);
            this.mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        this.mediaPlayer.setLooping(true);
    }

    @Override
    public void playSound(String audioName, int type) {
        //0 para musica de fondo
        if (type == 0){
            this.mediaPlayer.start();
        }
        //Sino es que se trata de un sonido denominado por audioName
        else{
            SoundApp sound = this.sounds.get(audioName);
            sound.play();
        }
    }

    //Activa el loop para el audio especificado
    @Override
    public void setLoop(String audioName) {
        Objects.requireNonNull(this.sounds.get(audioName)).setLoop(1);
    }
}
