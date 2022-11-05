package com.example.enginedesktop;

import com.example.lib.ISound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundDesktop implements ISound {

    AudioInputStream sound;

    Clip song;

    SoundDesktop(File file) {
        try{
            this.sound = AudioSystem.getAudioInputStream(file);
            this.song = AudioSystem.getClip();
            this.song.open(sound);
        }
        catch (IOException | UnsupportedAudioFileException | LineUnavailableException e){
            //AAA TODO QUITAR
            System.out.println("NO PUEDO CARGAR SONIDO");
            e.printStackTrace();
        }
    }

    //TODO CREO QUE FALTA UN GETTER DE AUDIOINPUTSTREAM



    @Override
    public void play() {

        this.song.setFramePosition(0);
        this.song.start();
    }

    @Override
    public void stop() {

    }

    @Override
    public void startLoop() {
        this.song.loop(Clip.LOOP_CONTINUOUSLY);
    }

}
