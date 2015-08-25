package lighterletter.c4q.nyc.momentone;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by gsyrimis on 8/23/15.
 */

public class SensorUtils {


    SensorManager sensorManager;

    private Acceleration acceleration;
    private Light light;
    private Temperature temperature;
    private Pressure pressure;
    private Humidity humidity;
    private Magnetic magnetic;

    public SensorUtils(Context ctx) {
        this.sensorManager = (SensorManager) ctx.getSystemService(ctx.SENSOR_SERVICE);
    }


    public float[] getAcceleration() {
        this.acceleration = new Acceleration();
        this.sensorManager.registerListener(this.acceleration,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        return new float[]{acceleration.x, acceleration.y, acceleration.z};
    }

    public float getLight() {
        this.light = new Light();
        this.sensorManager.registerListener(this.light,
                sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        return light.light;
    }

    public float getTemperature() {
        this.temperature = new Temperature();
        this.sensorManager.registerListener(this.temperature,
                sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_NORMAL);
        return temperature.temperature;
    }

    public float getPressure() {
        this.pressure = new Pressure();
        this.sensorManager.registerListener(this.pressure,
                sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
                SensorManager.SENSOR_DELAY_NORMAL);
        return pressure.pressure;
    }

    public float getHumidity() {
        this.humidity = new Humidity();
        this.sensorManager.registerListener(this.humidity,
                sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        return humidity.humidity;
    }

    public float[] getMagnetic() {

        this.magnetic = new Magnetic();
        this.sensorManager.registerListener(new SensorEventListener() {
                                                @Override
                                                public void onSensorChanged(SensorEvent event) {
//                                                    x = event.values[0];
//                                                    y = event.values[1];
//                                                    z = event.values[2];
                                                }

                                                @Override
                                                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                                }
                                            },
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
        return new float[]{magnetic.x, magnetic.y, magnetic.z};
    }






    private class Magnetic implements SensorEventListener {
        float x;
        float y;
        float z;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class Humidity implements SensorEventListener {
        float humidity;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                humidity = event.values[0];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class Pressure implements SensorEventListener {
        float pressure;

        public Pressure() {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
                pressure = event.values[0];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class Temperature implements SensorEventListener {
        float temperature;

        public Temperature() {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                temperature = event.values[0];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class Acceleration implements SensorEventListener {
        float x;
        float y;
        float z;

        public Acceleration() {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                z = event.values[2];
                x = event.values[0];
                y = event.values[1];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class Light implements SensorEventListener {
        float light;

        public Light() {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                light = event.values[0];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

}
