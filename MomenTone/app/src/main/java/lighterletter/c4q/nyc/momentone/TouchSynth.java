package lighterletter.c4q.nyc.momentone;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by c4q-john on 8/22/15.
 */
public class TouchSynth extends Activity {

    SineThread trackOne;

    SensorManager sensorManager;

    Button startBTN;
    Button stopBTN;
    float[] acceleration;
    float[] magnetic;
    float light;
    float humidity;
    float temperature;
    float pressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_synth);


        trackOne = new SineThread();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(new SensorEventListener() {
                                           @Override
                                           public void onSensorChanged(SensorEvent event) {
                                               trackOne.A_freq = event.values[0] * 40;
//                                               y = event.values[1];
                                               trackOne.C_square = event.values[2]*36;
                                           }

                                           @Override
                                           public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                           }
                                       },
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(new SensorEventListener() {
                                           @Override
                                           public void onSensorChanged(SensorEvent event) {
                                               trackOne.D_light_frequency = event.values[0] * 7;
                                           }

                                           @Override
                                           public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                           }
                                       }, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(new SensorEventListener() {
                                           @Override
                                           public void onSensorChanged(SensorEvent event) {
                                               trackOne.D_light_frequency = event.values[0] * 11;
                                           }

                                           @Override
                                           public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                           }
                                       }, sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_NORMAL);



        startBTN = (Button) findViewById(R.id.touch_start_btn);
        startBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!trackOne.isRunning()) {
                    trackOne.start();
                }


            }
        });
        stopBTN = (Button) findViewById(R.id.touch_stop_btn);
        stopBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trackOne.isRunning()) {
                    trackOne.isInterrupted();
                }


            }
        });
//
//        View mainView = findViewById(R.id.touch_main_view);
//        mainView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {   // on down touch
//                    case MotionEvent.ACTION_DOWN:
//                        trackOne.setAmplitude((int) event.getY());//volume
//                        trackOne.setFrequency(event.getX()); //pitch
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        trackOne.setAmplitude((int) event.getY());//volume
//                        trackOne.setFrequency(event.getX()); //pitch
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        trackOne.setAmplitude(0);
//                        trackOne.setFrequency(0);
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });

        // CREATE THREAD AND SOUND


    }


}
