package com.example.engineandroid;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
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

    private AssetManager assets;

    public AudioAndroid(){
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

        //Media player para la musica de fondo
        this.mediaPlayer = new MediaPlayer();

        //Aqui guardamos los sonidos de efecto
        this.sounds = new HashMap<>();
    }

    public void setAssetsManager(AssetManager assetsAux){
        this.assets = assetsAux;
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
        AssetFileDescriptor fileDescriptor = null;
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
        if (type == 0){
            this.mediaPlayer.start();
        }else{
            SoundApp sound = this.sounds.get(audioName);
            sound.play();
        }
    }

    @Override
    public void setLoop(String audioName) {
        this.sounds.get(audioName).setLoop(1);
    }
}
