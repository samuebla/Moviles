package com.example.engineandroid;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Objects;

//Encargado de reproducir sonidos previamente cargados en la escena
public class AudioAndroid {
    //Permite cambiar el path base de carga de ficheros
    private static final String PATH = "";
    //HashMap de sonidos para poder guardar y acceder a los sonidos mediante un string
    private final HashMap<String,SoundApp> sounds;

    //Contiene los sonidos pequeños
    private final SoundPool soundPool;

    //Para sonidos de mas de 1 mega (Para el background)
    private final MediaPlayer mediaPlayer;

    //Contiene todos los assets cargados desde la escena
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

    public void newSound(String audioName, String path) {
        //Guarda en los sounds y crea el nuevo sonido
        this.sounds.put(audioName, new SoundApp(this.soundPool, PATH + path, this.assets));
    }

    //Asigna la musica de fondo al mediaPlayer
    //Con este metodo no se reproduce, solo se carga. Para empezar a reproducir usar despues playSound con type = 0
    public void loadMusic(String path){
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

    //Reproduce un sonido.
    //type == 0 -> Sonido largo (solo puede haber uno y esta previamente asugnado en loadMusic)
    //type == 1 -> Sonido corto, solo reproduce el especificado por audioName
    public void playSound(String audioName, int type) {
        if (type == 0){
            this.mediaPlayer.start();
        }else{
            SoundApp sound = this.sounds.get(audioName);
            if (sound != null)
                sound.play();
        }
    }

    public void setLoop(String audioName) {
        Objects.requireNonNull(this.sounds.get(audioName)).setLoop(1);
    }
}
