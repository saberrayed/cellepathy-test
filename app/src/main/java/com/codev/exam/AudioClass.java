package com.codev.exam;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import java.io.File;
import java.util.HashMap;
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
//        maxVolume = audioManager.getStreamMaxVolume(audioManager.STREAM_MUSIC);
//        audioManager.setStreamVolume(audioManager.STREAM_MUSIC, maxVolume, 1);
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
//        MediaPlayer mp = MediaPlayer.create(activity, R.raw.audio);
//        mp.setAudioStreamType(AudioManager.MODE_IN_COMMUNICATION);
//        mp.setLooping(true);
//        mp.start();
    }

    public void shutdownTTS() {
        if (tts != null){
            tts.stop();
            tts.shutdown();
        }
    }

    public void playTTS(String speech) {
        /* if (speech == null|| "".equals(speech)) {
            speech = "Content not available";
            tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        } */

        String path = activity.getFilesDir().getAbsolutePath();
        HashMap<String, String> myHashRender = new HashMap();
        final String destFileName = path + "/tts-speech.wav";
        myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, speech);
        tts.synthesizeToFile(speech, myHashRender, destFileName);

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                final String keyword = s;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(activity, "Started" + keyword, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDone(String s) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(activity, "Done ", Toast.LENGTH_SHORT).show();
                        MediaPlayer mp = MediaPlayer.create(activity, Uri.fromFile(new File(destFileName)));
                        mp.setAudioStreamType(AudioManager.MODE_IN_COMMUNICATION);
                        // Hellomp.setLooping(true);
                        mp.start();
                    }
                });
            }

            @Override
            public void onError(String s) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Error on Saving file", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}