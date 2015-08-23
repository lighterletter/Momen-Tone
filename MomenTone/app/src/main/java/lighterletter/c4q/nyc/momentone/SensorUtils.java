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

    Acceleration acceleration;
    Light light;
    Temperature temperature;
    Pressure pressure;
    Humidity humidity;
    Magnetic magnetic;

    public SensorUtils(Context ctx) {
        this.sensorManager = (SensorManager) ctx.getSystemService(ctx.SENSOR_SERVICE);
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public Acceleration getAcceleration() {
        return acceleration;
    }

    public Light getLight() {
        return light;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Pressure getPressure() {
        return pressure;
    }

    public Humidity getHumidity() {
        return humidity;
    }

    public Magnetic getMagnetic() {
        return magnetic;
    }

    public void addAcceleration() {
        this.acceleration = new Acceleration();
        this.sensorManager.registerListener(this.acceleration,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void addLight() {
        this.light = new Light();
        this.sensorManager.registerListener(this.light,
                sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void addTemperature(){
        this.temperature = new Temperature();
        this.sensorManager.registerListener(this.temperature,
                sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void addPressure(){
        this.pressure = new Pressure();
        this.sensorManager.registerListener(this.pressure,
                sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void addHumidity(){
        this.humidity = new Humidity();
        this.sensorManager.registerListener(this.humidity,
                sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void addMagnetic(){
        this.magnetic = new Magnetic();
        this.sensorManager.registerListener(this.magnetic,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    private class Magnetic implements SensorEventListener{
        float x;
        float y;
        float z;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                z = event.values[2];
                x = event.values[0];
                y = event.values[1];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class Humidity implements SensorEventListener{
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

    private class Pressure implements SensorEventListener{
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

    private class Temperature implements SensorEventListener{
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
