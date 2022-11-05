package com.example.enginedesktop;

import com.example.lib.IAudio;
import com.example.lib.ISound;

import java.util.HashMap;

public class AudioDesktop implements IAudio {
    //Guardamos los sonidos
    private HashMap<String, SoundDesktop> sounds;

    public AudioDesktop() {
        sounds = new HashMap<>();
    }

    //SetMusic(String filepath)
    //StartMusic
    //Stop music??
    @Override
    public ISound newSound() {
        return null;
    }

    @Override
    public void playSound(String audioName) {


    }
}
