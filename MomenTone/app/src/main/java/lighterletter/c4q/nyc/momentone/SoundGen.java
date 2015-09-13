package lighterletter.c4q.nyc.momentone;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by c4q-john on 9/3/15.
 */
public class SoundGen {

    //waveform
    int amp = 10000;
    double twoPi = 8. * Math.atan(1.);
    double fr = 440;
    double ph = 0;
    final int SAMPLE_RATE = 11025 * 2;

    //frequencies

    //12 Note/Color  pairs mapped to sound. Ascending. Octave 4;
    public double fs4  = 369.0; //deep red
    public double g4   = 390.0; //light red
    public double gs4  = 415.0; //lighter red
    public double a    = 440.f;//orange/red
    public double a_b  = 466.164;//orange
    public double b    = 493.883;//chatreuse/  yellow-green
    public double c    = 523.251;//green
    public double c_d  = 554.365;//teal
    public double d    = 587.330;//sky blue
    public double d_e  = 622.254;//blue
    public double e    = 659.255;//purple-blue
    public double f    = 698.456;//indigo
    public double f_g  = 739.989;

    //pentatonic starting at 4th ocatave of A. for coding purposes its octave 1
    double gs5 = 830.609;
    double fs5 = 739.989;
    double ds5 = 622.254;
    double cs5 = 554.365;
    double b4 = 493.883;
    double a4 = 440.f;
    //octave 0
    double ds4 = 311.127;
    double cs4 = 277.183;
    double b3 = 246.942;
    double a3 = 220.f;

    //synth
    AudioSynthesisTask audioSynth;

    //Synth channels
    double fr_1;

    //scales/keys
    List<Double> pentatonic1;
    List<Double> pentatonic_0;

    boolean play_one = false;

    public SoundGen() {

        initNoteArrays();
        audioSynth = new AudioSynthesisTask();
        audioSynth.execute();
    }

    //shuffles notes
    public double shuffleArray(List<Double> list) {
        int index = (int) (Math.random() * list.size());
        Log.v("Index :", "" + index);
        return list.get(index);
    }

    private void initNoteArrays() {
        //music

        //oct 1
        pentatonic1 = new ArrayList<>();
        pentatonic1.add(gs5);
        pentatonic1.add(fs5);
        pentatonic1.add(ds5);
        pentatonic1.add(cs5);
        pentatonic1.add(b4);
        pentatonic1.add(a4);

        //oct 0
        pentatonic_0 = new ArrayList<>();
        pentatonic_0.add(gs4);
        pentatonic_0.add(fs4);
        pentatonic_0.add(ds4);
        pentatonic_0.add(cs4);
        pentatonic_0.add(b3);
        pentatonic_0.add(a3);
    }

    private class AudioSynthesisTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            int buffSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize, AudioTrack.MODE_STREAM);
            short samples[] = new short[buffSize];
            audioTrack.play();

            while (true) {

                if (play_one) {//On touch

                    audioTrack.play();

                    for (int l = 0; l < buffSize; l++) {
                        samples[l] = (short) (amp * Math.sin(ph));
                        ph += twoPi * fr_1 / SAMPLE_RATE;
                    }
                    audioTrack.write(samples, 0, buffSize);
                } else {
                    audioTrack.pause();
                    audioTrack.flush();
                    audioTrack.stop();
                }
            }
        }
    }
}
