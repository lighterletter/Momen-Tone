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


    //12 Note/Color  pairs mapped to sound. Ascending. Octave 4;
    double a    = 440.f;   //yellow
    double a_b  = 466.164; // 
    double b    = 493.883;
    double c    = 523.251;
    double c_d  = 554.365;
    double d    = 587.330;
    double d_e  = 622.254;
    double e    = 659.255;
    double f    = 698.456;
    double f_g  = 739.989;
    double g    = 783.991;
    double g_a  = 830.609;


    //waveform
    int amp = 100000;
    double twoPi = 16. * Math.atan(1.);
    double fr = 440;
    double ph = 1.0;
    final int SAMPLE_RATE = 11025 * 2;

    //frequencies
    //
    //C Major scale 3 octaves    c d,e,f,g,a,b,c
//    double c6 = 1046.50;
//    double b5 = 987.767;
//    double a5 = 880.f;
    //pentatonic starting at 4th ocatave of A. for coding purposes its octave 1
    double gs5 = 830.609;
    double fs5 = 739.989;
    double ds5 = 622.254;
    double cs5 = 554.365;
    double b4 = 493.883;
    double a4 = 440.f;
    //octave 0
    double gs4 = 415.305;
    double fs4 = 369.994;
    double ds4 = 311.127;
    double cs4 = 277.183;
    double b3 = 246.942;
    double a3 = 220.f;
    //octave -1


//    double g3 = 195.998;
//    double f3 = 174.614;
//    double e3 = 164.814;
//    double d3 = 146.832;
//    double c3 = 130.813;


    //synth
    AudioSynthesisTask audioSynth;

    //Synth channels
    double fr_1;
    double fr_2;
    double fr_3;
    double fr_4;

    double step_fr_1;
    double step_fr_2;
    double step_fr_3;

    //scales/keys
    List<Double> pentatonic1;
    List<Double> pentatonic_0;


    boolean play_one = false;
    boolean play_two = false;
    boolean play_three = false;
    boolean play_low = false;
    boolean play_all = false;

    //step detector boolean
    boolean step_registered = false;

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
        pentatonic1 = new ArrayList<>();
//        pentatonic1.add(c6);
//        pentatonic1.add(b5);
//        pentatonic1.add(a5);
        pentatonic1.add(gs5);
        pentatonic1.add(fs5);
        pentatonic1.add(ds5);
        pentatonic1.add(cs5);
        pentatonic1.add(b4);
        pentatonic1.add(a4);
        //octave 0
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
            AudioTrack audioTrack1 = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize, AudioTrack.MODE_STREAM);
            short samples1[] = new short[buffSize];
//            audioTrack1.play();
            AudioTrack audioTrack2 = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize, AudioTrack.MODE_STREAM);
            short samples2[] = new short[buffSize];
//            audioTrack2.play();
            AudioTrack lowAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize, AudioTrack.MODE_STREAM);
            short lowSamples[] = new short[buffSize];
//            lowAudioTrack.play();
            AudioTrack steptrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize, AudioTrack.MODE_STREAM);
            short stepsamples[] = new short[buffSize];
//            steptrack.play();

            while (true) {


                if (play_one || play_all) {//On touch

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


                if (play_two || play_all) {

                    audioTrack1.play();


                    for (int j = 0; j < buffSize; j++) {
                        samples1[j] = (short) ((2 / Math.PI) * Math.asin(Math.sin(ph)) * amp);
                        ph += twoPi * fr_2 / SAMPLE_RATE;
                    }
                    audioTrack1.write(samples1, 0, buffSize);
                } else {
                    audioTrack1.pause();
                    audioTrack1.flush();
                    audioTrack1.stop();
                }

                if (play_three || play_all) {
                    audioTrack2.play();
                    for (int n = 0; n < buffSize; n++) {
                        samples2[n] = (short) (amp * Math.cos(ph));
                        ph += twoPi * fr_3 / SAMPLE_RATE;
                    }
                    audioTrack2.write(samples2, 0, buffSize);
                } else {
                    audioTrack2.pause();
                    audioTrack2.flush();
                    audioTrack2.stop();
                }

                if (play_low) {
                    lowAudioTrack.play();
                    for (int n = 0; n < buffSize; n++) {
                        samples2[n] = (short) (amp * Math.sin(ph));
                        ph += twoPi * fr_4 / SAMPLE_RATE;
                    }
                    lowAudioTrack.write(lowSamples, 0, buffSize);
                } else {
                    lowAudioTrack.pause();
                    lowAudioTrack.flush();
                    lowAudioTrack.stop();
                }
                //step counter tracks
                if (step_registered) {//On  step
                    steptrack.play();
                    for (int l = 0; l < buffSize; l++) {
                        stepsamples[l] = (short) (amp * Math.sin(ph));
                        ph += twoPi * step_fr_1 / SAMPLE_RATE;
                    }
                    steptrack.write(stepsamples, 0, buffSize);
                } else {
                    steptrack.pause();
                    steptrack.flush();
                    steptrack.stop();
                }

            }
        }
    }
}
