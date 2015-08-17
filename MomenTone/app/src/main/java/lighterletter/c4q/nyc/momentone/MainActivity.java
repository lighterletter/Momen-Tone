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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private long diffTime;


    // audio threads
    Thread audioThreadOne;
    Thread audioThreadTwo;
    // sensormanager that gets registered with listeners
    SensorManager sensorManager;
    // audio tracks playing
    AudioTrack audioTrackOne;
    AudioTrack audioTrackTwo;
    // sample rate
    public static final int SAMPLE_RATE = 44100;
    //frequencies
    double putThisAsFrequencyA;
    double putThisAsFrequencyB;

    boolean isRunning = false;

    TextView axisValueTextView;
    EditText axisNum;

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

    SensorEventListener sensorEventListener;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // SETTING UP VIEWS
        axisValueTextView = (TextView) findViewById(R.id.main_axis_tv);

        // SETTING UP START BUTTON
        findViewById(R.id.main_start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // isRunning tells us if the audio is playing
                if (!isRunning) {
                    setupThreadOne();
                    setupThreadTwo();
                    audioThreadOne.start();
                    audioThreadTwo.start();
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
                if (audioThreadTwo.isAlive() && !audioThreadTwo.isInterrupted() && isRunning) {
                    isRunning = false;
                    audioTrackTwo.stop();
                    audioTrackTwo.release();
                    audioThreadTwo.interrupt();
                }
            }
        });

        // SETTING UP SENSOR
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    // assign directions
                    long curTime = System.currentTimeMillis();

                    if ((curTime - lastUpdate) > 100) {
                        diffTime = (curTime - lastUpdate);
                        lastUpdate = curTime;
                    }

                    float speed = (float)(Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000);

                    if (speed > SHAKE_THRESHOLD) {
                        putThisAsFrequencyA = playNote(event.values[0]);
                        putThisAsFrequencyB = playNote(event.values[1]);
                    }

                    last_x = x;
                    last_y = y;
                    last_z = z;


                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(
                sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
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

    // return note frequency
    public double playNote(float realtimeSensorValue) {

        if (realtimeSensorValue >= 9 && realtimeSensorValue < 10) {
            // Tilted up so scroll down
            return A;
        } else if (realtimeSensorValue >= 8 && realtimeSensorValue < 9) {
            // Tilted down so scroll up
            return AsBb;
        } else if (realtimeSensorValue >= 7 && realtimeSensorValue < 8) {
            // Tilted down so scroll up
            return B;
        } else if (realtimeSensorValue >= 6 && realtimeSensorValue < 7) {
            // Tilted down so scroll up
            return C;
        } else if (realtimeSensorValue >= 5 && realtimeSensorValue < 6) {
            // Tilted down so scroll up
            return CsDb;
        } else if (realtimeSensorValue >= 4 && realtimeSensorValue < 5) {
            // Tilted down so scroll up
            return D;
        } else if (realtimeSensorValue >= 3 && realtimeSensorValue < 4) {
            // Tilted down so scroll up
            return DsEb;
        } else if (realtimeSensorValue >= 2 && realtimeSensorValue < 3) {
            // Tilted down so scroll up
            return E;
        } else if (realtimeSensorValue >= 1 && realtimeSensorValue < 2) {
            // Tilted down so scroll up
            return F;
        } else if (realtimeSensorValue >= 0 && realtimeSensorValue < 1) {
            // Tilted down so scroll up
            return FsGb;
        } else if (realtimeSensorValue >= -1 && realtimeSensorValue < 0) {
            // Tilted down so scroll up
            return G;
        } else if (realtimeSensorValue >= -2 && realtimeSensorValue < -1) {
            // Tilted down so scroll up
            return GsAb;
        } else {
            // Stop scrolling
            return Aoc;
        }

    }

    // setting up thread with audiotrack of frequency B
    public void setupThreadTwo() {
        audioThreadTwo = new Thread() {
            public void run() {
                // Update isRunning value
                isRunning = true;
                // SETTING UP THE AUDIO PARAMS
                int minimumBufferSize = AudioTrack.getMinBufferSize(
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);
                audioTrackTwo = new AudioTrack(
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

                audioTrackTwo.play();

                // synthesis loop
                while (isRunning) {
                    frequency = putThisAsFrequencyB;
                    for (int i = 0; i < minimumBufferSize; i++) {
                        audioDataSamples[i] = (short) (amplitude * Math.sin(phase));
                        phase += twoPi * frequency / SAMPLE_RATE;
                    }
                    audioTrackTwo.write(audioDataSamples, 0, minimumBufferSize);
                }

            }
        };
    }

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
                    frequency = putThisAsFrequencyA;
                    for (int i = 0; i < minimumBufferSize; i++) {
                        audioDataSamples[i] = (short) (amplitude * Math.sin(phase));
                        phase += twoPi * frequency * 0.5 / SAMPLE_RATE;
                    }
                    audioTrackOne.write(audioDataSamples, 0, minimumBufferSize);
                }

            }
        };
    }
}
