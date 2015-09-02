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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class stepCounter extends Activity implements SensorEventListener  {


    SensorManager sensorManager;
    Button stop;
    Button start;

    //waveform
    int amp = 10000;
    double twoPi = 8. * Math.atan(1.);
    double fr = 440;
    double ph = 0.0;
    final int SAMPLE_RATE = 11025 * 2;

    // C Major scale 3 octaves    c d,e,f,g,a,b,c
//    double c6 = 1046.50;
//    double b5 = 987.767;
//    double a5 = 880.f;
    //pentatonic starting at 4th ocatave of A. for coding purposes its octave 1
    double gs5 = 830.609;
    double fs5 = 739.989;
    double ds5 = 622.254;
    double cs5 = 554.365;
    double b4 = 493.883;
    double a4 = 440.f;
    //octave 0
    double gs4 = 415.305;
    double fs4 = 369.994;
    double ds4 = 311.127;
    double cs4 = 277.183;
    double b3 = 246.942;
    double a3 = 220.f;
    //octave -1


//    double g3 = 195.998;
//    double f3 = 174.614;
//    double e3 = 164.814;
//    double d3 = 146.832;
//    double c3 = 130.813;

    //Synth
    double fr_1;
    double fr_2;
    double fr_3;
    double fr_4;

    double step_fr_1;
    double step_fr_2;
    double step_fr_3;

    TextView xCoor;
    TextView yCoor;
    TextView zCoor;

    float x;
    float y;
    float z;

    List<Double> pentatonic1;
    List<Double> pentatonic_0;

    AudioSynthesisTask audioSynth;

    boolean play_one = false;
    boolean play_two = false;
    boolean play_three = false;
    boolean play_low = false;
    boolean play_all = false;

    boolean step_registered = false;

    boolean sensor_state = true;

    //shuffles notes
    private double shuffleArray(List<Double> list) {
        int index = (int) (Math.random() * list.size());
        Log.v("Index :", "" + index);
        return list.get(index);
    }

    //draw instance vars
    //represents the instance of the custom VIew that we added to the layout.
    DrawingView drawView;
    //paint color button in the pallete
    ImageButton currPaint;
    //need layout that contains button to retrieve the first paint color in the palette.
    LinearLayout paintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //drawing
        drawView = (DrawingView) findViewById(R.id.drawing);
        paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        //get the first button and store it as the instance variable:
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        // sets alternate options for when the button is pressed.
        currPaint.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.paint_pressed));


        //music
        pentatonic1 = new ArrayList<>();
//        pentatonic1.add(c6);
//        pentatonic1.add(b5);
//        pentatonic1.add(a5);
        pentatonic1.add(gs5);
        pentatonic1.add(fs5);
        pentatonic1.add(ds5);
        pentatonic1.add(cs5);
        pentatonic1.add(b4);
        pentatonic1.add(a4);
        //octave 0

        pentatonic_0 = new ArrayList<>();
        pentatonic_0.add(gs4);
        pentatonic_0.add(fs4);
        pentatonic_0.add(ds4);
        pentatonic_0.add(cs4);
        pentatonic_0.add(b3);
        pentatonic_0.add(a3);

//        pentatonic1.add(g3);
//        pentatonic1.add(f3);
//        pentatonic1.add(e3);
//        pentatonic1.add(d3);
//        pentatonic1.add(c3);


        //audiothread
        audioSynth = new AudioSynthesisTask();
        audioSynth.execute();

        //sensor listeners
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

//        View touchView = this.findViewById(R.id.drawing);
//        touchView.setOnTouchListener(this);

        //Play-Pause button.
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final PlayPauseDrawable mPlayPauseDrawable = new PlayPauseDrawable(60, 0XFF101840, 0XFFffffff, 300);
        imageView.setImageDrawable(mPlayPauseDrawable);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  xor operator: ( ^= )  is 12% percent faster, and more efficient than
                // other alternatives like: bool = !bool; or bool = bool ? false : true;

                //toggles sound
                sensor_state ^= true;

                mPlayPauseDrawable.toggle();

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        play_one = false;
        play_two = false;
        play_three = false;
        finish();
        sensor_state = false;

    }
    //inserted from Main activity to quickly test compatibility. No
//    public void paintClicked(View view){
//        //use chosen color
//        if (view != currPaint){
//            //update color
//            ImageButton imgView = (ImageButton)view;
//            String color = view.getTag().toString();
//            //after receiving the color tag call setColor method in DrawingView  on the custom drawing View Object:
//            drawView.setColor(color);
//            //update UI to reflect chosen color and reset previous one
//            imgView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.paint_pressed));
//            currPaint.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.paint));
//            currPaint = (ImageButton) view;
//        }
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction();
//        switch (action)
//        {
//            case MotionEvent.ACTION_DOWN:
//                play_one = true;
//                fr_1 = event.getX(); //pitch
//                amp = (int)event.getY();//volume
//                Log.v("FREQUENCY", ""+fr_1);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                play_all = true;
//                fr_1 = event.getX();
//                amp = (int) event.getY();//
//                Log.v("FREQUENCY", ""+fr_1);
//                break;
//            case MotionEvent.ACTION_UP:
//                play_all = false;
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    //regular code resumes
    @Override
    public void onSensorChanged(SensorEvent event) {
        //pedometer
        if (sensor_state) {

            if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                // Success! There's a step detector.


                step_fr_1 = shuffleArray(pentatonic_0);
                step_fr_2 = shuffleArray(pentatonic_0);
                step_fr_3 = shuffleArray(pentatonic_0);
                step_registered = true;



                Log.v("this is pedometer: ", "" + step_fr_1);

            } else {
                step_registered = false;

                // Failure! No pedometer.
                Log.v("SensorDetect: ", "No Pedometer");
            }
            //light
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                for (int i = 0; i < event.values[0] % Math.random(); i++) {
                    play_one = true;
                    play_two = true;
                    play_three = true;
                    fr_1 = shuffleArray(pentatonic1);
                    fr_2 = shuffleArray(pentatonic1);
                    fr_3 = shuffleArray(pentatonic1);
                }
                play_one = false;
                play_two = false;
                play_three = false;

                Log.v("SensorDetect: ", "This is light: " + event.values[0]);

            } else {
                // No light
                Log.v("SensorDetect: ", "No Light Sensor");

            }

            // Accelerometer

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
//
////                                                   if (x > 3.f || x < -3.f || z < 9.f) {
////                                                   play_one = true;
////                                                   play_two = true;
////                                                   play_three = true;
////                                                   fr_1 = shuffleArray(pentatonic1);
////                                                   fr_2 = shuffleArray(pentatonic1);
////                                                   fr_3 = shuffleArray(pentatonic1);
////                                                   } else if (x > 4) {
////                                                       play_one = false;
////                                                       play_two = false;
////                                                       play_three = false;
////                                                   }

                if (y >= 2 || y <= -2) {
//
                    for (int i = 0; i < event.values[0] % Math.random(); i++) {
                        play_one = true;
//                    play_two = true;
//                    play_three = true;
                        fr_1 = shuffleArray(pentatonic_0);
//                    fr_2 = shuffleArray(pentatonic1);
//                    fr_3 = shuffleArray(pentatonic1);
                    }
                    //else{
//                    play_one = false;
                    //}
                }
//                play_two = false;
//                play_three = false;

//                Log.v("SensorDetect: ", "This is light: " + event.values[0]);
                }
//                    play_all = true;
//                    fr_1 = shuffleArray(pentatonic1);
//                    fr_2 = shuffleArray(pentatonic1);
//                    fr_3 = shuffleArray(pentatonic1);
//                }
////                else if (z < 4) {
////                    fr_1 = shuffleArray(pentatonic_0);
////                    fr_2 = shuffleArray(pentatonic_0);
////                    fr_3 = shuffleArray(pentatonic_0);
////                }
//                else {
//                    play_all = false;
//                }

//                Log.v("SensorDetectAccel: ", "Accel x: "+event.values[0]+ "y: " +event.values[1]+"z: "+ event.values[2]);


            //  } else {
            // No accelerometer
            //     Log.v("SensorDetect: ", "No Pedometer");
                 //}
        } else {
            //no accelerometer
        }
    }
    //event listener for drawing portion. This belongs here.


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


            AudioTrack steptrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffSize, AudioTrack.MODE_STREAM);
            short stepsamples[] = new short[buffSize];
            steptrack.play();








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


                //step counter tracks
                if (step_registered) {//On  step

                    for (int l = 0; l < buffSize; l++) {
                        stepsamples[l] = (short) (amp * Math.sin(ph));
                        ph += twoPi * step_fr_1 / SAMPLE_RATE;
                    }
                    steptrack.write(stepsamples, 0, buffSize);
                }

            }
        }
    }
}
