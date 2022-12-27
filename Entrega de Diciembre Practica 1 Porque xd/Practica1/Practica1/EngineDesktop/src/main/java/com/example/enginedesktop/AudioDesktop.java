package com.example.enginedesktop;

import com.example.lib.IAudio;
import com.example.lib.ISound;

import java.io.File;
import java.util.HashMap;

public class AudioDesktop implements IAudio {
    private static final String PATH = "";
    //Guardamos los sonidos
    private final HashMap<String, SoundDesktop> sounds;
    SoundDesktop backgroundMusic;

    public AudioDesktop() {
        sounds = new HashMap<>();
    }

    //Inicializa un sonido
    @Override
    public ISound newSound(String audioName,String path) {
        File audioFile = new File(PATH + path);
        SoundDesktop sAux = new SoundDesktop(audioFile);
        //Lo metemos en el mapa
        sounds.put(audioName,sAux);
        return sAux;
    }

    //Inicializa la musica de fondo
    @Override
    public void loadMusic(String audioName, String path) {
        File audioFile = new File(PATH + path);

        //Guardamos la musica de fondo, la cual sera solo una
        this.backgroundMusic = new SoundDesktop(audioFile);
    }

    //Reproduce un sonido, si type es 0 se reproduce la musica de fondo, sino reproduce el sonido especificado
    @Override
    public void playSound(String audioName, int type) {
        if (type == 0){
            this.backgroundMusic.startLoop();
        }else{
            sounds.get(audioName).play();
        }
    }

    //Reproduce en bucle el sonido especificado
    @Override
    public void setLoop(String audioName){
        sounds.get(audioName).startLoop();
    }
}
