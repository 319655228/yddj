package com.hckj.yddxst.utils;

import android.content.Context;
import android.media.AudioManager;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * @author Richie on 2019.03.23
 * 声音管理
 */
public final class AudioObserver implements LifecycleObserver {

    private Context mContext;

    public AudioObserver(Context context) {
        mContext = context;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void oStop() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
    }


}
