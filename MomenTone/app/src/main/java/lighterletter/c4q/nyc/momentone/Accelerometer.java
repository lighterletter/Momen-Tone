package lighterletter.c4q.nyc.momentone;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

/**
 * Created by c4q-john on 8/10/15.
 */
public class Accelerometer extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;

    TextView xCoor; // declare X axis object
    TextView yCoor; // declare Y axis object
    TextView zCoor; // declare Z axis object

    float x;
    float y;
    float z;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        xCoor = (TextView) findViewById(R.id.xcoor); // create X axis object
        yCoor = (TextView) findViewById(R.id.ycoor); // create Y axis object
        zCoor = (TextView) findViewById(R.id.zcoor); // create Z axis object

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

		/*	More sensor speeds (taken from api docs)
            SENSOR_DELAY_FASTEST get sensor data as fast as possible
		    SENSOR_DELAY_GAME	rate suitable for games
		 	SENSOR_DELAY_NORMAL	rate (default) suitable for screen orientation changes
		*/
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {

        // check sensor type
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // assign directions
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            xCoor.setText("X: " + x);
            yCoor.setText("Y: " + y);
            zCoor.setText("Z: " + z);
        }
    }
}


