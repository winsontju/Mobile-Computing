package com.example.winsontju;

import android.content.Context;
import android.media.SoundPool;
import android.os.Build;

import androidx.annotation.RequiresApi;

class SoundManager {
    private Context context;
    private SoundPool pool;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    SoundManager(Context context){
        this.context = context;
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(10);
        pool = builder.build();
    }
    int addSound(int resourceID){
        return pool.load(context, resourceID,1);
    }

    void play(int soundID){
        pool.play(soundID, 1, 1, 1, 0, 1);
    }
}
