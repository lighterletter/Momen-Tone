package lighterletter.c4q.nyc.momentone;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by gsyrimis on 8/23/15.
 */
public class SquareThread extends Thread{
    private AudioTrack audioTrack;

    private boolean isRunning = false;

    private static final int SAMPLE_RATE = 44100;

    private double frequency = 442;

    private int amplitude = 0;

    public SquareThread() {
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

        double angle = 0;

        while (isRunning) {
            for (int i = 0; i < minimumBufferSize; i++) {
                double angularFrequency = (frequency % 300) < 100 ? 600 : 0;
                angle += angularFrequency;
                audioDataSamples[i] = (short) (amplitude * ((float) Math.cos(angle)));
            }
//                    synth_frequency = synth_frequency - 20; //effect test.
            audioTrack.write(audioDataSamples, 0, minimumBufferSize);
        }
    }



    @Override
    public synchronized void start() {
        isRunning = true;
        super.start();
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public double getFrequency() {
        return frequency;
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
