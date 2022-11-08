package com.example.enginedesktop;

import com.example.lib.IAudio;
import com.example.lib.ISound;

import java.io.File;
import java.util.HashMap;

public class AudioDesktop implements IAudio {
    private static final String PATH = "";
    //Guardamos los sonidos
    //private HashMap<String, SoundDesktop> sounds;

    public AudioDesktop() {
        //sounds = new HashMap<>();
    }

    //SetMusic(String filepath)
    //StartMusic
    //Stop music??
    @Override
    public ISound newSound(String audioName) {
        File audioFile = new File(PATH + audioName);
        return new SoundDesktop(audioFile);
    }

    @Override
    public void playSound(String audioName) {
        ISound s = newSound(audioName);
        s.play();
    }
}
