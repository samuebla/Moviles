package com.example.engineandroid;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.example.lib.ISound;

public class SoundApp implements ISound {

    private SoundPool soundPool;

    private int soundID;
    private float volume = 1.0f;
    private int priority = 0;
    private int loop = 0;
    private float rate = 1.0f;

    public SoundApp(SoundPool sPool, String path, AssetManager assets) {
        String newFilePath = path.replaceAll("assets/", "");
        this.soundPool = sPool;
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = assets.openFd(newFilePath);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.soundID = this.soundPool.load(fileDescriptor, this.priority);
    }

    public void setLoop(int loopAux){
        this.loop = loopAux;
    }

    public void setPriority(int priorityAux){
        this.priority = priorityAux;
    }

    public void setRate(float rateAux){
        this.rate = rateAux;
    }

    public void setVolume(float volumeAux){
        this.volume = volumeAux;
    }

    @Override
    public void play() {
        this.soundPool.play(this.soundID, this.volume, this.volume, this.priority, this.loop, this.rate);
    }

    @Override
    public void stop() {
        this.soundPool.stop(soundID);
    }

    @Override
    public void startLoop() {
        this.loop = 1;
    }

}
