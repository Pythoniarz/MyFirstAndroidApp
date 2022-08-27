package com.example.myfirstapplication;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class SimpleSoundPlayer extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onPlayClicked(View v) {
//        playNoise();
        playSineWave();
    }

    public void onStopClicked(View v) {
        stop();
    }

    boolean mStop = false;
    AudioTrack mAudiotrack;
    Thread mAudioThread;

    Runnable mNoiseGenerator = new Runnable() {
        public void run() {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

            /* 8000 bytes per second, 1000 bytes = 125 ms */
            byte[] noiseData = new byte[1000];
            Random rnd = new Random();

            while (!mStop) {
                rnd.nextBytes(noiseData);
                mAudiotrack.write(noiseData, 0, noiseData.length);
            }
        }
    };

    Runnable mSineWaveGenerator = new Runnable() {
        public void run() {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

//            /* 8000 bytes per second, 1000 bytes = 125 ms */
//            byte [] noiseData = new byte[1000];
//            Random rnd = new Random();
            // Sine wave
            double[] mSound = new double[2 * 44100];
            short[] mBuffer = new short[2 * 44100];
            for (int i = 0; i < mSound.length; i++) {
                mSound[i] = Math.sin((2.0 * Math.PI * 440.0 / 44100.0 * (double) i));
                mBuffer[i] = (short) (mSound[i] * Short.MAX_VALUE);
            }

            while (!mStop) {
                mAudiotrack.write(mBuffer, 0, mSound.length);
            }
        }
    };

    void playNoise() {
        mStop = false;

        /* 8000 bytes per second*/
//        mAudiotrack = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_OUT_MONO,
//                AudioFormat.ENCODING_PCM_8BIT, 8000 /* 1 second buffer */,
//                AudioTrack.MODE_STREAM);

        mAudiotrack = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_UNKNOWN)
                        .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_8BIT)
                        .setSampleRate(8000)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build())
                .setBufferSizeInBytes(8000)
                .build();

        mAudiotrack.play();


        mAudioThread = new Thread(mNoiseGenerator);
        mAudioThread.start();
    }

    private void playSineWave() {

        mStop = false;
        // AudioTrack definition
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        mAudiotrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);

        mAudiotrack = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_UNKNOWN)
                        .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(44100)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build())
                .setBufferSizeInBytes(44100)
                .build();

//        // Sine wave
//        double[] mSound = new double[2*44100];
//        short[] mBuffer = new short[2*44100];
//        for (int i = 0; i < mSound.length; i++) {
//            mSound[i] = Math.sin((2.0*Math.PI * 440.0/44100.0*(double)i));
//            mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
//        }

//        mAudioTrack.setStereoVolume(0.1f, 0.1f);
        mAudiotrack.play();
        mAudioThread = new Thread(mSineWaveGenerator);
//        mAudiotrack.write(mBuffer, 0, mSound.length);
        mAudioThread.start();
//        mAudiotrack.release();

    }

    void stop() {
        mStop = true;
        mAudiotrack.stop();
    }
}