package lighterletter.c4q.nyc.momentone;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by c4q-john on 9/3/15.
 */
public class SensorListener extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;

    float x;
    float y;
    float z;

    //step detector boolean
    boolean step_registered = false;
    //general sensor boolean
    boolean sensor_state = true;


    SoundGen synth;


    public SensorListener(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.sensor_state = true;

        //sensor listeners
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

//        synth = new SoundGen();

    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction();
//        switch (action)
//        {
//            case MotionEvent.ACTION_DOWN:
//
//                synth.play_one = true;
//                synth.fr_1 = event.getX(); //pitch
//                synth.amp = (int)event.getY();//volume
//                Log.v("FREQUENCY", "" + synth.fr_1);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                synth.play_all = true;
//                synth.fr_1 = event.getX();
//                synth.amp = (int) event.getY();//
//                Log.v("FREQUENCY", ""+ synth.fr_1);
//                break;
//            case MotionEvent.ACTION_UP:
//                synth.play_all = false;
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    public SensorListener() {


        //sensor listeners
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
