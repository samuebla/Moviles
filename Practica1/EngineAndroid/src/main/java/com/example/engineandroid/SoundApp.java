package com.example.engineandroid;

import android.media.AudioAttributes;
import android.media.SoundPool;

import com.example.lib.ISound;

public class SoundApp implements ISound {

    private SoundPool soundPool;
    private int soundID;

    public SoundApp(SoundPool sPool, String path) {
        this.soundPool = sPool;
        soundID = this.soundPool.load(path, 0);
    }

    @Override
    public void play() {
        this.soundPool.stop(soundID);
        this.soundPool.play(this.soundID, 1, 1, 0, 0, 1);
    }

    @Override
    public void stop() {
        this.soundPool.stop(soundID);
    }

    @Override
    public void startLoop() {
        this.soundPool.setLoop(soundID, -1);
    }

}
