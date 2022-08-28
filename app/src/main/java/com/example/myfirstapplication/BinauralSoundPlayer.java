package com.example.myfirstapplication;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;

public class BinauralSoundPlayer extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onPlayClicked(View v) {
        playSineWave();
    }

    public void onStopClicked(View v) {
        stop();
    }

    boolean mStop = false;
    AudioTrack mAudiotrack;
    Thread mAudioThread;
    double leftFreq = 440;
    double rightFreq = 220;

    Runnable mSineWaveGenerator = new Runnable() {
        public void run() {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

            /* 8000 bytes per second, 1000 bytes = 125 ms */

            // Sine wave
            double[] mSound = new double[2 * 44100];
            short[] mBuffer = new short[2 * 44100];

            for (int i = 0; i < mSound.length; i++) {
                // 2 * pi * freq / bitrate
                if (i % 2 == 0) {
                    mSound[i] = Math.sin((2.0 * Math.PI * rightFreq / 44100.0 * (double) i));
                } else {
                    mSound[i] = Math.sin((2.0 * Math.PI * leftFreq / 44100.0 * (double) i));
                }
                mBuffer[i] = (short) (mSound[i] * Short.MAX_VALUE);
            }

            double TmpLeftFreq = leftFreq;
            double TmpRightFreq = rightFreq;

            while (!mStop) {
                // check if need to recalculate buffer
                if (TmpLeftFreq != leftFreq || TmpRightFreq != rightFreq) {
                    for (int i = 0; i < mSound.length; i++) {
                        // 2 * pi * freq / bitrate
                        if (i % 2 == 0) {
                            mSound[i] = Math.sin((2.0 * Math.PI * rightFreq / 44100.0 * (double) i));
                        } else {
                            mSound[i] = Math.sin((2.0 * Math.PI * leftFreq / 44100.0 * (double) i));
                        }
                        mBuffer[i] = (short) (mSound[i] * Short.MAX_VALUE);
                    }
                    TmpLeftFreq = leftFreq;
                    TmpRightFreq = rightFreq;
                }
                mAudiotrack.write(mBuffer, 0, mSound.length);
            }
        }
    };

    private void playSineWave() {
        mStop = false;
        /*
        // AudioTrack definition
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        mAudiotrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);

         */
        mAudiotrack = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_UNKNOWN)
                        .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(44100)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                        .build())
                .setBufferSizeInBytes(44100)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build();

        mAudiotrack.play();
        mAudioThread = new Thread(mSineWaveGenerator);

        mAudioThread.start();
    }

    void stop() {
        mStop = true;
        mAudiotrack.stop();
    }
}

/*    Generate Sine wave of a particular frequency :
private static byte[] generateSineWavefreq(int frequencyOfSignal, int seconds) {
        // total samples = (duration in second) * (samples per second)
        byte[] sin = new byte[seconds * sampleRate];
        double samplingInterval = (double) (sampleRate / frequencyOfSignal);
        System.out.println("Sampling Frequency  : "+sampleRate);
        System.out.println("Frequency of Signal : "+frequencyOfSignal);
        System.out.println("Sampling Interval   : "+samplingInterval);
        for (int i = 0; i < sin.length; i++) {
        double angle = (2.0 * Math.PI * i) / samplingInterval;
        sin[i] = (byte) (Math.sin(angle) * 127);
        System.out.println("" + sin[i]);
        }
        return sin;
        }

 */