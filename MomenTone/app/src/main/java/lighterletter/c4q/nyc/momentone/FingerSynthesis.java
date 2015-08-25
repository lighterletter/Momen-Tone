package lighterletter.c4q.nyc.momentone;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FingerSynthesis extends Activity {

    AudioSynthesisTask audioSynth;
    static final float BASE_FREQUENCY = 440;
    float synth_frequency = BASE_FREQUENCY;
    float low_frequency_oscillator = 12;
    static final float baseAmplitude = 800;
    float amplitude = baseAmplitude; //variable mapped to the y axis of the event input, in this case the touch sensor.
    boolean play = false; // used to determine when we should actually be playing sound or not base on touch event.

    TextView yCoor;
    TextView xCoor;
    TextView amplitude_TextV;
    TextView frequency_TextV;
    TextView lfo_TextV;
    Button tone;

    String sample_buffer_String;
    String sample_buffer_length;


    SensorEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        Accelerometer accelerometer = new Accelerometer();

//        amplitude = baseAmplitude * accelerometer.x;
//        View mainView = this.findViewById(R.id.MainView);
//        mainView.setOnTouchListener(this);

        audioSynth = new AudioSynthesisTask();
        audioSynth.execute();

        yCoor = (TextView) findViewById(R.id.ycoor);
        xCoor = (TextView) findViewById(R.id.xcoor);

        yCoor.setText("length: " + sample_buffer_length);
        xCoor.setText("int: " + sample_buffer_String);

        amplitude_TextV = (TextView) findViewById(R.id.amplitude);
        frequency_TextV = (TextView) findViewById(R.id.frequency);
        lfo_TextV = (TextView) findViewById(R.id.lfo);
        amplitude_TextV.setText("Amplitude: " + amplitude);
        frequency_TextV.setText("Pitch: " + synth_frequency);
        lfo_TextV.setText("lfo :" + low_frequency_oscillator);


        tone = (Button) findViewById(R.id.tone);
        tone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play = true;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        play = false;
        finish();
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                play = true;
//                synth_frequency = event.getX() *-1; //pitch
//                amplitude = -event.getY()*-1;//volume
//                yCoor.setText(amplitude+"");
//                xCoor.setText(synth_frequency+"");
//
//                amplitude_TextV.setText("Amplitude: " + amplitude);
//                frequency_TextV.setText("Pitch: " +synth_frequency);
//                lfo_TextV.setText("lfo :" +low_frequency_oscillator);
//                Log.v("FREQUENCY", "" + synth_frequency);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                play = true;
//                synth_frequency = event.getX()*-1;
//                amplitude = -event.getY()*-1;//
//
//                yCoor.setText(amplitude+"");
//                xCoor.setText(synth_frequency+"");
//
//                amplitude_TextV.setText("Amplitude: "+ amplitude);
//                frequency_TextV.setText("Pitch: " +synth_frequency);
//                lfo_TextV.setText("lfo :" +low_frequency_oscillator);
//
//
//                Log.v("FREQUENCY", "" + synth_frequency);
//                break;
//            case MotionEvent.ACTION_UP:
//                play = false;
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//            default:
//                break;
//        }
//        return true;
//    }


    private class AudioSynthesisTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            final int SAMPLE_RATE = 44100;
            int minSize = AudioTrack.getMinBufferSize( //Sample size. Individual sample.
                    SAMPLE_RATE,                    // Numerical cycles per second
                    AudioFormat.CHANNEL_OUT_MONO,   // Channel to output
                    AudioFormat.ENCODING_PCM_16BIT  // pulse code modulation giving you
                    // encoding at 16 bits per cycle to define a sample rate.
            );
            AudioTrack audioTrack = new AudioTrack( //connects the calculation to the hardware
                    AudioManager.STREAM_MUSIC,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minSize,
                    AudioTrack.MODE_STREAM);
            audioTrack.play();
            AudioTrack audioTrack2 = new AudioTrack( //connects the calculation to the hardware
                    AudioManager.STREAM_MUSIC,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minSize,
                    AudioTrack.MODE_STREAM);
            audioTrack2.play();

            short[] sample_buffer = new short[minSize]; //will be fed as a string.
            float angle = 0;
            //LFO
            while (true) {

                if (play) {//On touch

                    for (int j = 0; j < sample_buffer.length; j++) {
//                        float lfo_frequency = (float) (2 * Math.PI) * low_frequency_oscillator / SAMPLE_RATE;
//                        angle += lfo_frequency;
//
//                        float frequency = (float) ((2 * Math.PI) * synth_frequency / SAMPLE_RATE); //initialize bit modulation.
//                        angle += frequency; //creates wave

                        angle = (short) (low_frequency_oscillator * 2 * (-0.02/1200 + 0.5 * j));
                        sample_buffer[j] = (short) (amplitude * Math.cos(angle)); //give it an angle
                    }
                    //}

                    //

                    audioTrack.write(sample_buffer, 0, sample_buffer.length);

                }

                if (play)
                {
                    for ( int i = 0; i < sample_buffer.length; i++) {
                        float angular_frequency =  (float) (2 * Math.PI) * synth_frequency / SAMPLE_RATE;
                        angle += angular_frequency;
                        sample_buffer[i] = (short) (amplitude * ((float) Math.cos(angle)));


                        //sample_buffer = (short)(amplitude * Math.Sign(Math.sin(angle * i)));
                    }
//                    audioTrack.write(sample_buffer, 0 , sample_buffer.length);
//                    synth_frequency = synth_frequency - 20; //effect test.
                    audioTrack2.write(sample_buffer,0,sample_buffer.length);//test track
                }
//                if (play)
//                {
//                    for ( int i = 0; i < sample_buffer.length; i++)
//                    {
//
//
//                        float angular_frequency = (float) (2 * Math.PI) * (synth_frequency/2) / SAMPLE_RATE;//(synth_frequency++ % 6) <3 ? 3 :0;
//                        //(short) (2*Math.PI) * synth_frequency / SAMPLE_RATE;
//                        angle += angular_frequency;
//                        sample_buffer[i] = (short) (amplitude * ((float)Math.cos(angle)));
//                    }
////                    audioTrack.write(sample_buffer, 0 , sample_buffer.length);
////                    synth_frequency = synth_frequency - 20; //effect test.
//                    audioTrack3.write(sample_buffer,0,sample_buffer.length);//test track
//                }
//            }
            }
        }
    }
}
