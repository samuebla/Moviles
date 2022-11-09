package com.example.enginedesktop;

import com.example.lib.IAudio;
import com.example.lib.ISound;

import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.Clip;

public class AudioDesktop implements IAudio {
    private static final String PATH = "";
    //Guardamos los sonidos
    private HashMap<String, SoundDesktop> sounds;
    SoundDesktop backgroundMusic;

    public AudioDesktop() {
        sounds = new HashMap<>();
    }

    //SetMusic(String filepath)
    //StartMusic
    //Stop music??
    @Override
    public ISound newSound(String audioName,String path) {
        File audioFile = new File(PATH + path);
        SoundDesktop sAux = new SoundDesktop(audioFile);
        //Lo metemos en el mapa
        sounds.put(audioName,sAux);
        return sAux;
    }

    @Override
    public void loadMusic(String audioName, String path) {
        File audioFile = new File(PATH + path);
        SoundDesktop sAux = new SoundDesktop(audioFile);
        sAux.startLoop();

        this.backgroundMusic = sAux;
    }

    @Override
    public void playSound(String audioName, int type) {
        if (type == 0){
            this.backgroundMusic.play();
        }else{
            sounds.get(audioName).play();
        }
    }

    @Override
    public void setLoop(String audioName){
        sounds.get(audioName).startLoop();
    }
}
