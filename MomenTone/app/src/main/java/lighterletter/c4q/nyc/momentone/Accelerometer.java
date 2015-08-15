package lighterletter.c4q.nyc.momentone;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Timer;


/**
 * Created by c4q-john on 8/10/15.
 */
public class Accelerometer extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    TextView xCoor; // declare X axis object
    TextView yCoor; // declare Y axis object
    TextView zCoor; // declare Z axis object

    private int duration = 1; // seconds
    private int sampleRate = 4000;
    private int numSamples = duration * sampleRate;
    private double sample[] = new double[numSamples];

    //Can we/should we store an array of notes?
    private double A    = 440.00;   // hz  "Concert A" A above middle C
    private double AsBb = 466.16;   // hz: A#/Bb
    private double B    = 493.88;   // hz: B
    private double C    = 532.25;   // hz: C one octave higher than middle C
    private double CsDb = 554.37;   // hz: C#/Db (C sharp/ D flat(minor))
    private double D    = 587.33;
    private double DsEb = 622.25;
    private double E    = 659.26;
    private double F    = 698.46;
    private double FsGb = 739.99;
    private double G    = 783.99;
    private double GsAb = 830.61;
    private double Aoc  = 880.00;   // hz: A one octave above "Concert A"

    private byte generatedSnd[] = new byte[2 * numSamples];

    Handler handler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        xCoor = (TextView) findViewById(R.id.xcoor); // create X axis object
        yCoor = (TextView) findViewById(R.id.ycoor); // create Y axis object
        zCoor = (TextView) findViewById(R.id.zcoor); // create Z axis object

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // add listener. The listener will be HelloAndroid (this) class
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
		/*	More sensor speeds (taken from api docs)
            SENSOR_DELAY_FASTEST get sensor data as fast as possible
		    SENSOR_DELAY_GAME	rate suitable for games
		 	SENSOR_DELAY_NORMAL	rate (default) suitable for screen orientation changes
		*/

    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Runnable runnable = new Runnable() {
            public void run() {
                // Use a new tread as this can take a while
                final Thread thread = new Thread(new Runnable() {
                    public void run() {
                        genTone();
                        handler.post(new Runnable() {

                            public void run() {
                                playSound();
                            }
                        });
                    }
                });
                thread.start();
            }

            void genTone() {
                // fill out the array
                for (int i = 0; i < numSamples; ++i) {
                    sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / A));
                }
                // convert to 16 bit pcm sound array
                // assumes the sample buffer is normalised.
                int idx = 0;
                for (final double dVal : sample) {
                    // scale to maximum amplitude
                    final short val = (short) ((dVal * 32767));
                    // in 16 bit wav PCM, first byte is the low order byte
                    generatedSnd[idx++] = (byte) (val & 0x00ff);
                    generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
                }
            }
            void playSound() {
                final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                        sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                        AudioTrack.MODE_STATIC);
                audioTrack.write(generatedSnd, 0, generatedSnd.length);
                audioTrack.play();
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }
//    public void accelerate(View v){
//
//    }

    public void onSensorChanged(SensorEvent event) {

        // check sensor type
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // assign directions
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            A = A * y;
            Log.v("The value of A: ", "" +A);

            xCoor.setText(String.valueOf(x));
            yCoor.setText(String.valueOf(y));
            zCoor.setText(String.valueOf(z));
        }

    }
}


