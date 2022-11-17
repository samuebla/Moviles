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

    Clip clip;

    public SoundDesktop(File file) {
        try{
            //Abre y reproduce un sonido
            this.sound = AudioSystem.getAudioInputStream(file);
            this.clip = AudioSystem.getClip();
            this.clip.open(sound);
        }
        catch (IOException | UnsupportedAudioFileException | LineUnavailableException e){
            System.out.println("Can't load audio file");
            e.printStackTrace();
        }
    }

    @Override
    public void play() {
        this.clip.setFramePosition(0);
        this.clip.start();
    }

    @Override
    public void stop() {
        if(this.clip.isRunning())
            this.clip.stop();
    }

    @Override
    public void startLoop() {
        this.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

}
