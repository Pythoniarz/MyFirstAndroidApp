package com.example.myfirstapplication;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Bundle;
import android.util.TypedValue;
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
    int samplingRate = 44100;
    double leftFreq = 220;
    double rightFreq = 212.5;

    Runnable mSineWaveGenerator = new Runnable() {
        public void run() {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

            /* 8000 bytes per second, 1000 bytes = 125 ms */;

            // Sine wave
            double[] mSound = new double[2 * samplingRate];
            short[] mBuffer = new short[2 * samplingRate];

            for (int i = 0; i < mSound.length; i++) {
                // 2 * pi * freq / bitrate
                if (i % 2 == 0) {
                    mSound[i] = Math.sin((2.0 * Math.PI * rightFreq / samplingRate * (double) i));
                } else {
                    mSound[i] = Math.sin((2.0 * Math.PI * leftFreq / samplingRate * (double) i));
                }
                mBuffer[i] = (short) (mSound[i] * Short.MAX_VALUE);
            }

            double tmpLeftFreq = leftFreq;
            double tmpRightFreq = rightFreq;
            int tmpSamplingRate = samplingRate;

            while (!mStop) {
                // check if need to recalculate buffer
                if (tmpSamplingRate != samplingRate) {
                    mSound = new double[2 * samplingRate];
                    mBuffer = new short[2 * samplingRate];

                    tmpSamplingRate = samplingRate;
                }

                if (tmpLeftFreq != leftFreq || tmpRightFreq != rightFreq) {
                    for (int i = 0; i < mSound.length; i++) {
                        // 2 * pi * freq / bitrate
                        if (i % 2 == 0) {
                            mSound[i] = Math.sin((2.0 * Math.PI * rightFreq / samplingRate * i));
                        } else {
                            mSound[i] = Math.sin((2.0 * Math.PI * leftFreq / samplingRate * i));
                        }
                        mBuffer[i] = (short) (mSound[i] * Short.MAX_VALUE);
                    }
                    tmpLeftFreq = leftFreq;
                    tmpRightFreq = rightFreq;
                }
                mAudiotrack.write(mBuffer, 0, mSound.length);
            }
        }
    };

    private void playSineWave() {
        mStop = false;
        /*
        // AudioTrack definition
        int mBufferSize = AudioTrack.getMinBufferSize(samplingRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        mAudiotrack = new AudioTrack(AudioManager.STREAM_MUSIC, samplingRate,
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
                        .setSampleRate(samplingRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                        .build())
                .setBufferSizeInBytes(samplingRate)
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

    public void setBitrate(int parseInt) {
        samplingRate = parseInt;
    }

//    private int getSamplingRate() {
//        TypedValue typedValue = new TypedValue();
//        getResources().getValue(R.integer.sampling_rate, typedValue, true);
//        int samplingRate = (int) typedValue.getFloat();
//        return samplingRate;
//    };
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