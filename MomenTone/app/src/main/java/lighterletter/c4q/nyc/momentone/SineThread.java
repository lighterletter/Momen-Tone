package lighterletter.c4q.nyc.momentone;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by gsyrimis on 8/23/15.
 */
public class SineThread extends Thread {

    private AudioTrack audioTrack;
    private boolean isRunning = false;

    private static final int SAMPLE_RATE = 44100;

    double A_freq = 420; //A

    private int amplitude = 10000;

    double C_square = 532.25;   //C
    double D_light_frequency = 587.33;    //D
    double Af_temperature_frequency = 880; //A

    public SineThread() {
        super();
    }

    @Override
    public void run() {
        isRunning = true;
        int minimumBufferSize = AudioTrack.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minimumBufferSize,
                AudioTrack.MODE_STREAM);

        short[] audioDataSamples = new short[minimumBufferSize];

        audioTrack.play();

        double angleA = 0;
        double angleB = 0;
        double angleC = 0;
        double angleD = 0;

        while (isRunning) {

            for (int i = 0; i < minimumBufferSize; i++) {
                // instead of two pi try 3 pi
                double angularFrequencyA = (float) (2 * Math.PI) * A_freq / SAMPLE_RATE;

                angleA += angularFrequencyA;
                // instead of sine use cosine
                short a = (short) (amplitude * ((float) Math.sin(angleA)));
                double angularFrequencyB = (float) (2 * Math.PI) * C_square / SAMPLE_RATE;
                angleB += angularFrequencyB;
                short b = (short) (amplitude * ((float) Math.sin(angleB)));

                double angularFrequencyC = (float) (2 * Math.PI) * D_light_frequency / SAMPLE_RATE;
                angleC += angularFrequencyC;
                short c = (short) (amplitude * ((float) Math.sin(angleC)));
//
//                double angularFrequencyD = (float) (2 * Math.PI) * Af_temperature_frequency / SAMPLE_RATE;
//                angleD += angularFrequencyD;
//                short d = (short) (amplitude * ((float) Math.sin(angleD)));

                audioDataSamples[i] = (short)(a+b+c);
            }



            audioTrack.write(audioDataSamples, 0, minimumBufferSize);
        }
    }


    @Override
    public synchronized void start() {
        super.start();
    }

    public void setFrequency(double frequency) {
        this.A_freq = frequency;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public double getFrequency() {
        return A_freq;
    }

    public int getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(int amplitude) {
        this.amplitude = amplitude;
    }

    public void kill() {
        if (this.isAlive() && !this.isInterrupted() && isRunning) {
            isRunning = false;
            audioTrack.stop();
            audioTrack.release();
            this.interrupt();
        }
    }
}
