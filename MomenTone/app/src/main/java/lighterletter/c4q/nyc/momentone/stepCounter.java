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

import org.w3c.dom.Text;

public class stepCounter extends Activity implements SensorEventListener {

    SensorManager sensorManager;
    Button tone;

    int amp = 1000;
    double twoPi = 8. * Math.atan(1.);
    double fr = Math.random() * 440;
    double ph = 0.0;
    final int sr = 44100;


    double fr_a = 440.f;
    double fr_c = 523.251;
    double fr_g = 783.991;
    double fr_e = 659.255;
    double fr_d = 587.33;

    double fr_1;
    double fr_2;
    double fr_3;

    TextView xCoor;
    TextView yCoor;
    TextView zCoor;

    float x;
    float y;
    float z;


    AudioSynthesisTask audioSynth;
    boolean play = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        audioSynth = new AudioSynthesisTask();
        audioSynth.execute();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        xCoor = (TextView) findViewById(R.id.xcoor);
        yCoor = (TextView) findViewById(R.id.ycoor);
        zCoor = (TextView) findViewById(R.id.zcoor);


    }

    @Override
    protected void onPause() {
        super.onPause();
        play = false;
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        xCoor.setText(""+event.values[0]);
        yCoor.setText(""+ event.values[1]);
        zCoor.setText(""+event.values[2]);

        if (event.values[0] >= 1) {
            fr_1 = fr_g;//a
            fr_2 = fr_e;
            fr_3 = fr_a;
//                fr = 440.f;//a
        } else if (event.values[1] >= 1) {
            fr_1 = fr_g;//a
            fr_2 = fr_c;
            fr_3 = fr_a;
            //fr = 783.991;//g
        } else if (event.values[2] <= 9) {
            fr_1 = fr_g;//a
            fr_2 = fr_d;
            fr_3 = fr_a;
            // fr = 523.251;//c
        }
//        for (int i = 0; i < 12000; i++) {
            play = true;
//            i++;
            Log.v("this is i: ", "" + fr);
//        }
        play = false;
        Log.v("this is pedometer: ", "" + event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class AudioSynthesisTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            int buffSize = AudioTrack.getMinBufferSize(sr, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sr, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize, AudioTrack.MODE_STREAM);
            short samples[] = new short[buffSize];
            audioTrack.play();


            AudioTrack audioTrack1 = new AudioTrack(AudioManager.STREAM_MUSIC, sr, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize, AudioTrack.MODE_STREAM);
            short samples1[] = new short[buffSize];
            audioTrack1.play();

            AudioTrack audioTrack2 = new AudioTrack(AudioManager.STREAM_MUSIC, sr, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize, AudioTrack.MODE_STREAM);
            short samples2[] = new short[buffSize];
            audioTrack2.play();


            while (true) {
                if (play) {//On touch
                    for (int l = 0; l < buffSize; l++) {
                        samples[l] = (short) (amp * Math.sin(ph));
                        ph += twoPi * fr_1 / sr;
                    }
                    audioTrack.write(samples, 0, buffSize);

                    for (int j = 0; j < buffSize; j++) {
                        samples1[j] = (short) (amp * Math.sin(ph));
                        ph += twoPi * fr_2 / sr;
                    }
                    audioTrack1.write(samples1, 0, buffSize);
                    for (int n = 0; n < buffSize; n++) {
                        samples2[n] = (short) (amp * Math.sin(ph));
                        ph += twoPi * fr_3 / sr;
                    }
                    audioTrack2.write(samples2, 0, buffSize);
                }
            }
        }
    }
}
