package com.codev.exam;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private TextView txtMessage;
    private EditText edtSpeech;
    private AudioClass audio;


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            txtMessage.setText("Device found!");
        }
        else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            txtMessage.setText("Device is now connected.");
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            txtMessage.setText("Done searching device.");
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
            txtMessage.setText("Device is about to disconnect...");
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            txtMessage.setText("Device is disconnected.");
        }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        txtMessage = findViewById(R.id.txtMessage);
        edtSpeech = findViewById(R.id.edtSpeech);
        audio = new AudioClass(MainActivity.this);

        if (!isBluetoothHeadsetConnected()) {
            txtMessage.setText("Waiting for device.");
        } else {
            txtMessage.setText("Device is now connected.");
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);

        findViewById(R.id.btnPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // audio.playAudio();
            // txtMessage.setText("Now playing audio..\nTo restart program, please close the app.");

            String speech = edtSpeech.getText().toString();
            audio.playTTS(speech);
            txtMessage.setText("Now playing '" + speech + "'");
            }
        });
    }

    public static boolean isBluetoothHeadsetConnected() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED;
    }

    @Override
    protected void onPause() {
        audio.shutdownTTS();
        super.onPause();
    }
}
