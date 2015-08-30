package lighterletter.c4q.nyc.momentone;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class stepCounter extends Activity implements SensorEventListener {

    SensorManager sensorManager;
    Button stop;
    Button start;

    //waveform
    int amp = 1000;
    double twoPi = 8. * Math.atan(1.);
    double fr = 440;
    double ph = 0.0;
    final int SAMPLE_RATE = 44100;

    // C Major scale 3 octaves    c d,e,f,g,a,b,c
    double c6 = 1046.50;
    double b5 = 987.767;
    double a5 = 880.f;
    double g5 = 783.991;
    double f5 = 698.456;
    double e5 = 783.991;
    double d5 = 587.330;
    double c5 = 523.251;
    double b4 = 493.883;
    double a4 = 440.f;
    double g4 = 391.995;
    double f4 = 349.228;
    double e4 = 329.628;
    double d4 = 293.665;
    double c4 = 261.626;
    double b3 = 246.942;
    double a3 = 220.f;
    double g3 = 195.998;
    double f3 = 174.614;
    double e3 = 164.814;
    double d3 = 146.832;
    double c3 = 130.813;

    //Synth
    double fr_1;
    double fr_2;
    double fr_3;
    double fr_4;

    TextView xCoor;
    TextView yCoor;
    TextView zCoor;

    float x;
    float y;
    float z;

    List<Double> C_Major;

    AudioSynthesisTask audioSynth;

    boolean play_one = false;
    boolean play_two = false;
    boolean play_three = false;
    boolean play_low = false;
    boolean play_all = false;

    private double shuffleArray(List<Double> list) {
        int index = (int) (Math.random() * list.size());
        Log.v("Index :", "" + index);
        return list.get(index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        C_Major = new ArrayList<>();
        C_Major.add(c6);
        C_Major.add(b5);
        C_Major.add(a5);
        C_Major.add(g5);
        C_Major.add(f5);
        C_Major.add(e5);
        C_Major.add(d5);
        C_Major.add(c5);
        C_Major.add(b4);
        C_Major.add(a4);
        C_Major.add(g4);
        C_Major.add(f4);
        C_Major.add(e4);
        C_Major.add(d4);
        C_Major.add(c4);
        C_Major.add(b3);
        C_Major.add(a3);
        C_Major.add(g3);
        C_Major.add(f3);
        C_Major.add(e3);
        C_Major.add(d3);
        C_Major.add(c3);

        xCoor = (TextView) findViewById(R.id.xcoor);
        yCoor = (TextView) findViewById(R.id.ycoor);
        zCoor = (TextView) findViewById(R.id.zcoor);

        //customList.add(clean);
        audioSynth = new AudioSynthesisTask();
        audioSynth.execute();


        stop = (Button) findViewById(R.id.main_stop_btn);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_one = false;
                play_two = false;
                play_three = false;
            }
        });

        start = (Button) findViewById(R.id.main_start_btn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_one = true;
                play_two = true;
                play_three = true;
                for (int i = 0; i < Math.random() * 5; i++) {
                    fr_1 = shuffleArray(C_Major);
                    fr_2 = shuffleArray(C_Major);
                    fr_3 = shuffleArray(C_Major);
                }

            }
        });
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(new SensorEventListener() {
                                           @Override
                                           public void onSensorChanged(SensorEvent event) {

                                               if (event.values[0] >= 1) {

                                                   long runningTime = System.currentTimeMillis();

                                                   long currTime = runningTime - System.currentTimeMillis();

                                                   Log.v("currtime", "" + currTime);

                                                   for (int i = 0; i < currTime; i++) {
                                                       play_all = true;
                                                       //Track switches
//                                                       play_one = true;
//                                                       play_two = true;
//                                                       play_three = true;
                                                       //set frequencies
                                                       fr_1 = a4;
                                                       fr_2 = f4;
                                                       fr_3 = d4;
//                                                       play_one = false;
//                                                       play_two = false;
//                                                       play_three = false;
//                                                       play_all = false;
                                                   }
                                               }
                                               Log.v("this is pedometer: ", "" + event.values[0]);
                                           }

                                           @Override
                                           public void onAccuracyChanged(Sensor sensor, int accuracy) {
                                           }
                                       },
                sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(new SensorEventListener() {
                                           @Override
                                           public void onSensorChanged(SensorEvent event) {
//                                               play = true;
//                                               fr_1 = shuffleArray(C_Major);
//                                               play = false;
                                               //}

                                               for (int i = 0; i < event.values[0] % Math.random(); i++) {
                                                   play_one = true;
                                                   play_two = true;
                                                   play_three = true;
                                                   fr_1 = shuffleArray(C_Major);
                                                   fr_2 = shuffleArray(C_Major);
                                                   fr_3 = shuffleArray(C_Major);
                                               }
                                               play_one = false;
                                               play_two = false;
                                               play_three = false;

                                           }

                                           @Override
                                           public void onAccuracyChanged(Sensor sensor, int accuracy) {
                                           }
                                       }, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(new SensorEventListener() {
                                           @Override
                                           public void onSensorChanged(SensorEvent event) {
                                               x = event.values[0];
                                               y = event.values[1];
                                               z = event.values[2];

                                               if (x > 3.f || x < -3.f || z < 9.f) {
//                                                   play_one = true;
//                                                   play_two = true;
//                                                   play_three = true;
//                                                   fr_1 = shuffleArray(C_Major);
//                                                   fr_2 = shuffleArray(C_Major);
//                                                   fr_3 = shuffleArray(C_Major);
                                               } else if (x > 4) {
                                                   play_one = false;
                                                   play_two = false;
                                                   play_three = false;
                                               }
                                               play_one = false;

//                                               if (y >= 3 || y < -3) {
//                                                   fr_1 = shuffleArray(C_Major);
//                                                   fr_2 = shuffleArray(C_Major);
//                                                   fr_3 = shuffleArray(C_Major);
//                                               }
//                                               else {
////                                                   amp = (int) Math.random() * 1000;
//                                               }

                                               xCoor.setText("" + x);
                                               yCoor.setText("" + y);
                                               zCoor.setText("" + z);
                                           }

                                           @Override
                                           public void onAccuracyChanged(Sensor sensor, int accuracy) {
                                           }
                                       }, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(new SensorEventListener() {
                                           @Override
                                           public void onSensorChanged(SensorEvent event) {
                                               play_low = true;
                                               fr_4 = 220.f;

                                           }

                                           @Override
                                           public void onAccuracyChanged(Sensor sensor, int accuracy) {
                                           }
                                       }, sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        play_one = false;
        play_two = false;
        play_three = false;
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
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
            audioTrack1.play();
            AudioTrack audioTrack2 = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize, AudioTrack.MODE_STREAM);
            short samples2[] = new short[buffSize];
            audioTrack2.play();

            AudioTrack lowAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize, AudioTrack.MODE_STREAM);
            short lowSamples[] = new short[buffSize];
            lowAudioTrack.play();
            while (true) {

                if (play_one || play_all) {//On touch
                    for (int l = 0; l < buffSize; l++) {
                        samples[l] = (short) (amp * Math.sin(ph));
                        ph += twoPi * fr_1 / SAMPLE_RATE;
                    }
                    audioTrack.write(samples, 0, buffSize);
                }
                if (play_two || play_all) {
                    for (int j = 0; j < buffSize; j++) {
                        samples1[j] = (short) (amp * Math.sin(ph));
                        ph += twoPi * fr_2 / SAMPLE_RATE;
                    }
                    audioTrack1.write(samples1, 0, buffSize);
                }
                if (play_three || play_all) {
                    for (int n = 0; n < buffSize; n++) {
                        samples2[n] = (short) (amp * Math.sin(ph));
                        ph += twoPi * fr_3 / SAMPLE_RATE;
                    }
                    audioTrack2.write(samples2, 0, buffSize);
                }
                if (play_low || play_all) {
                    for (int n = 0; n < buffSize; n++) {
                        samples2[n] = (short) (amp * Math.sin(ph));
                        ph += twoPi * fr_4 / SAMPLE_RATE;
                    }
                    lowAudioTrack.write(lowSamples, 0, buffSize);
                }
            }
        }
    }
}
