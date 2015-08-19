package lighterletter.c4q.nyc.momentone;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // TODO MODULATE VOLUME FOR QUANTIZED SOUND

    // sensormanager that gets registered with listeners
    SensorManager sensorManager;
    SensorEventListener sensorEventListener;

    public static final int SAMPLE_RATE = 44100;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 200;
    private long diffTime;

    boolean isRunning = false;

    // audio threads
    Thread audioThreadOne;
    AudioTrack audioTrackOne;
    double putThisAsFrequencyA;

    // I LIKE 2 THREADS BUT FOR SIMPLICITY I'M GOING BACK TO ONE
//    Thread audioThreadTwo;
//    AudioTrack audioTrackTwo;
//    double putThisAsFrequencyB;


    private double A = 440.00;   // hz  "Concert A" A above middle C
    private double AsBb = 466.16;   // hz: A#/Bb
    private double B = 493.88;   // hz: B
    private double C = 532.25;   // hz: C one octave higher than middle C
    private double CsDb = 554.37;   // hz: C#/Db (C sharp/ D flat(minor))
    private double D = 587.33;
    private double DsEb = 622.25;
    private double E = 659.26;
    private double F = 698.46;
    private double FsGb = 739.99;
    private double G = 783.99;
    private double GsAb = 830.61;
    private double Aoc = 880.00;


    TextView xResponseLV;
    TextView yResponseLV;
    TextView zResponseLV;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xResponseLV = (TextView) findViewById(R.id.main_x_responses_lv);
        yResponseLV = (TextView) findViewById(R.id.main_y_responses_lv);
        zResponseLV = (TextView) findViewById(R.id.main_z_responses_lv);

        xResponseLV.setMovementMethod(new ScrollingMovementMethod());
        yResponseLV.setMovementMethod(new ScrollingMovementMethod());
        zResponseLV.setMovementMethod(new ScrollingMovementMethod());


        // SETTING UP START BUTTON
        findViewById(R.id.main_start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // isRunning tells us if the audio is playing
                if (!isRunning) {
                    setupThreadOne();
//                    setupThreadTwo();
                    audioThreadOne.start();
//                    audioThreadTwo.start();
                }

            }
        });

        // STOP BUTTON
        findViewById(R.id.main_stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioThreadOne.isAlive() && !audioThreadOne.isInterrupted() && isRunning) {
                    isRunning = false;
                    audioTrackOne.stop();
                    audioTrackOne.release();
                    audioThreadOne.interrupt();
                }
//                if (audioThreadTwo.isAlive() && !audioThreadTwo.isInterrupted() && isRunning) {
//                    isRunning = false;
//                    audioTrackTwo.stop();
//                    audioTrackTwo.release();
//                    audioThreadTwo.interrupt();
//                }
            }
        });

        // SETTING UP SENSOR
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                    putThisAsFrequencyA = event.values[0] * 10;
                }


            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(
                sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);


    }


    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        try {
            audioThreadOne.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audioThreadOne = null;
    }

    public boolean between(float value, int small, int big) {
        if (value >= small && value < big)
            return true;
        return false;
    }

    // return note frequency
    public double playNote(float realtimeSensorValue) {
        if (between(realtimeSensorValue, 11, 10)) {
            return GsAb * 0.5;
        } else if (between(realtimeSensorValue, 9, 10)) {
            return A;
        } else if (between(realtimeSensorValue, 8, 9)) {
            return AsBb;
        } else if (between(realtimeSensorValue, 7, 8)) {
            return B;
        } else if (between(realtimeSensorValue, 6, 7)) {
            return C;
        } else if (between(realtimeSensorValue, 5, 6)) {
            // Tilted down so scroll up
            return CsDb;
        } else if (between(realtimeSensorValue, 4, 5)) {
            // Tilted down so scroll up
            return D;
        } else if (between(realtimeSensorValue, 3, 4)) {
            // Tilted down so scroll up
            return DsEb;
        } else if (between(realtimeSensorValue, 2, 3)) {
            // Tilted down so scroll up
            return E;
        } else if (between(realtimeSensorValue, 1, 2)) {
            // Tilted down so scroll up
            return F;
        } else if (between(realtimeSensorValue, 0, 1)) {
            // Tilted down so scroll up
            return FsGb;
        } else if (between(realtimeSensorValue, -1, 0)) {
            // Tilted down so scroll up
            return G;
        } else if (between(realtimeSensorValue, -2, -1)) {
            // Tilted down so scroll up
            return GsAb;
        } else if (between(realtimeSensorValue, -3, -2)) {
            // Tilted down so scroll up
            return A * 2;
        } else if (between(realtimeSensorValue, -4, -3)) {
            // Tilted down so scroll up
            return AsBb * 2;
        } else if (between(realtimeSensorValue, -5, -4)) {
            // Tilted down so scroll up
            return B * 2;
        } else if (between(realtimeSensorValue, -6, -5)) {
            // Tilted down so scroll up
            return C * 2;
        } else if (between(realtimeSensorValue, -7, -6)) {
            // Tilted down so scroll up
            return CsDb * 2;
        } else if (between(realtimeSensorValue, -8, -7)) {
            // Tilted down so scroll up
            return D * 2;
        } else if (between(realtimeSensorValue, -9, -8)) {
            // Tilted down so scroll up
            return DsEb * 2;
        } else if (between(realtimeSensorValue, -10, -9)) {
            // Tilted down so scroll up
            return E * 2;
        } else if (between(realtimeSensorValue, -11, -10)) {
            // Tilted down so scroll up
            return F * 2;
        } else {
            // Stop scrolling
            return 0;
        }

    }

    // setting up thread with audiotrack of frequency B
//    public void setupThreadTwo() {
//        audioThreadTwo = new Thread() {
//            public void run() {
//                // Update isRunning value
//                isRunning = true;
//                // SETTING UP THE AUDIO PARAMS
//                int minimumBufferSize = AudioTrack.getMinBufferSize(
//                        SAMPLE_RATE,
//                        AudioFormat.CHANNEL_OUT_MONO,
//                        AudioFormat.ENCODING_PCM_16BIT);
//                audioTrackTwo = new AudioTrack(
//                        AudioManager.STREAM_MUSIC,
//                        SAMPLE_RATE,
//                        AudioFormat.CHANNEL_OUT_MONO,
//                        AudioFormat.ENCODING_PCM_16BIT,
//                        minimumBufferSize,
//                        AudioTrack.MODE_STREAM);
//
//                // SOUNDTRACK VARIABLES
//                short[] audioDataSamples = new short[minimumBufferSize];
//                int amplitude = 10000;
//                double twoPi = 8. * Math.atan(1.);
//                double frequency = 440.f;
//                double phase = 0.0;
//
//                audioTrackTwo.play();
//
//                // synthesis loop
//                while (isRunning) {
//                    frequency = putThisAsFrequencyB;
//                    for (int i = 0; i < minimumBufferSize; i++) {
//                        audioDataSamples[i] = (short) (amplitude * Math.sin(phase));
//                        phase += twoPi * frequency / SAMPLE_RATE;
//                    }
//                    audioTrackTwo.write(audioDataSamples, 0, minimumBufferSize);
//                }
//
//            }
//        };
//    }

    // setting up thread with audiotrack of frequency A
    public void setupThreadOne() {
        audioThreadOne = new Thread() {
            public void run() {
                // Update isRunning value
                isRunning = true;
                // SETTING UP THE AUDIO PARAMS
                int minimumBufferSize = AudioTrack.getMinBufferSize(
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);
                audioTrackOne = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        minimumBufferSize,
                        AudioTrack.MODE_STREAM);

                // SOUNDTRACK VARIABLES
                short[] audioDataSamples = new short[minimumBufferSize];
                int amplitude = 10000;
                double twoPi = 8. * Math.atan(1.);
                double frequency = 440.f;
                double phase = 0.0;

                audioTrackOne.play();

                // synthesis loop
                while (isRunning) {
                    // TODO HERE WE CAN CHANGE FREQUENCIES
                    frequency = putThisAsFrequencyA;
                    for (int i = 0; i < minimumBufferSize; i++) {
                        audioDataSamples[i] = (short) (amplitude * Math.sin(phase));
                        phase += twoPi * frequency * 0.8 / SAMPLE_RATE;
                    }
                    audioTrackOne.write(audioDataSamples, 0, minimumBufferSize);
                }

            }
        };
    }
}
