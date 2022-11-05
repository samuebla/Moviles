package com.example.enginedesktop;

import com.example.lib.ISound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundDesktop implements ISound {

    AudioInputStream sound;

    SoundDesktop(File file) {
        try{
            sound = AudioSystem.getAudioInputStream(file);
        }
        catch (IOException | UnsupportedAudioFileException e){
            //AAA TODO QUITAR
            System.out.println("NO PUEDO CARGAR SONIDO");
            e.printStackTrace();
        }

    }

    //TODO CREO QUE FALTA UN GETTER DE AUDIOINPUTSTREAM



    @Override
    public void play() {
    }

    @Override
    public void stop() {

    }

}
