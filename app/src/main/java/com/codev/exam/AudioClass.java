package com.codev.exam;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class AudioClass
{
    private AudioManager audioManager;
    private Activity activity;
    private int maxVolume;

    public AudioClass(Activity activity)
    {
        this.activity = activity;
        audioManager = (AudioManager)
                activity.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        maxVolume = audioManager.getStreamMaxVolume(audioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(audioManager.STREAM_MUSIC, maxVolume, 1);
        audioManager.setSpeakerphoneOn(true);
    }

    public void playAudio() {
        MediaPlayer mp = MediaPlayer.create(activity, R.raw.audio);
        mp.setAudioStreamType(AudioManager.MODE_IN_COMMUNICATION);
        mp.setLooping(true);
        mp.start();
    }
}