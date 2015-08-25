package lighterletter.c4q.nyc.momentone;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by gsyrimis on 8/20/15.
 */
public class AudioThread extends Thread {

    private AudioTrack audioTrack;
    private boolean isRunning = false;
    private static final int SAMPLE_RATE = 44100;
    private double frequency = 0;
    private int amplitude = 10000;
    private static final double twoPi = 8. * Math.atan(1.);
    private double phase = 0.0;
    private double angle = 0;
    private int minimumBufferSize;
    private short[] audioDataSamples;
    private double angular_frequency;




    public AudioThread(int waveType) {
        super();
    }

    @Override
    public void run() {
        isRunning = true;
        minimumBufferSize = AudioTrack.getMinBufferSize(
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
        audioDataSamples = new short[minimumBufferSize];

        audioTrack.play();

        while (isRunning) {
            for (int i = 0; i < minimumBufferSize; i++) {
                angular_frequency = (float) (2 * Math.PI) * frequency / SAMPLE_RATE;
                angle += angular_frequency;
                audioDataSamples[i] = (short) (amplitude * ((float) Math.sin(angle)));
            }
            audioTrack.write(audioDataSamples, 0, minimumBufferSize);
        }
    }

    private void makeSine() {
        for (int i = 0; i < minimumBufferSize; i++) {
            angular_frequency = (float) (2 * Math.PI) * frequency / SAMPLE_RATE;
            angle += angular_frequency;
            audioDataSamples[i] = (short) (amplitude * ((float) Math.sin(angle)));
        }
        audioTrack.write(audioDataSamples, 0, minimumBufferSize);

    }

    private void makeSquare() {

        for (int i = 0; i < minimumBufferSize; i++) {
            angular_frequency = (frequency++ % 6) < 3 ? 3 : 0;
            angle += angular_frequency;
            audioDataSamples[i] = (short) (amplitude * ((float) Math.cos(angle)));
        }
//                    synth_frequency = synth_frequency - 20; //effect test.
        audioTrack.write(audioDataSamples, 0, minimumBufferSize);

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

    public double getPhase() {
        return phase;
    }

    public void setPhase(double phase) {
        this.phase = phase;
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
