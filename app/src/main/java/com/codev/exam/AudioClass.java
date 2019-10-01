package com.codev.exam;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class AudioClass
{
    private TextToSpeech tts;
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

        tts = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result=tts.setLanguage(Locale.US);

                    if (result ==TextToSpeech.LANG_MISSING_DATA ||
                        result== TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    } else {
                        // playTTS();
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });
    }

    public void playAudio() {
        MediaPlayer mp = MediaPlayer.create(activity, R.raw.audio);
        mp.setAudioStreamType(AudioManager.MODE_IN_COMMUNICATION);
        mp.setLooping(true);
        mp.start();
    }

    public void shutdownTTS() {
        if (tts != null){
            tts.stop();
            tts.shutdown();
        }
    }

    public void playTTS(String speech) {
        if (speech == null|| "".equals(speech)) {
            speech = "Content not available";
            tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}