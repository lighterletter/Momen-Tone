package lighterletter.c4q.nyc.momentone;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;




/**
 * Created by c4q-john on 8/21/15.
 */
public class Magnetometer extends Activity implements SensorEventListener {


        //  <tpc(transplant code) imports

        Thread audioThread_One;
        boolean isRunning = false;

        SeekBar seekBar;
        double seekbarValue;

        AudioTrack audioTrack;
        SensorManager sensorManager;


        public static final int SAMPLE_RATE = 44100;
        double X;
        double noteValue;


        TextView axisValueTextView; // declare Z axis object
        TextView xCoor;
        TextView yCoor;
        TextView ZCoor;
        TextView noteText;

        public static double A = 440.00;   // hz  "Concert A" A above middle C
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
        private double Aoc = 220;

        private float degVal;


        //  /tcp>

        // define the display assembly compass picture
        private TextView image;
        // record the compass picture angle turned
        private double currentDegree;
        // device sensor manager
        private SensorManager mSensorManager;
        TextView tvHeading;
        TextView tvBarometer;
        final NotePicker notePicker = new NotePicker();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_accelerometer);
            //
            //image = (TextView) findViewById(R.id.main_stop_btn);
            // TextView that will tell the user what degree is he heading
            yCoor = (TextView) findViewById(R.id.ycoor);
            // initialize your android device sensor capabilities
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


            noteText = (TextView) findViewById(R.id.note_text);


            xCoor = (TextView) findViewById(R.id.xcoor);



            //seekBar = (SeekBar) findViewById(R.id.main_textview);
            //axisValueTextView = (TextView) findViewById(R.id.xCoor);

        }

        @Override
        protected void onResume() {
            super.onResume();
            // for the system's orientation sensor registered listeners
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_GAME);
        }

        @Override
        protected void onPause() {
            super.onPause();
            // to stop the listener and save battery
            mSensorManager.unregisterListener(this);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // get the angle around the z-axis rotated
            final double degree = event.values[0];
            noteText.setText("A: " + noteValue + "Heading: " + degree + " degrees");
            currentDegree = -degree;



            //tonal logic gate
            // assign direction Values:
            // x = 0
            // y = 1
            // z = 2
            X = event.values[0];
//        Y = event.values[1];


            xCoor.setText("X : " + String.valueOf(X));
//
//            if (degree > 0 && degree < 10) {
//                // Tilted up so scroll down
//                noteValue = A;
//                noteText.setText("A: " + noteValue + "Heading: " + Float.toString(degree) + " degrees");
//
//            } else if (degree > 10 && degree < 15) {
//                // Tilted down so scroll up
//                noteValue = AsBb;
//                noteText.setText("AsBb: " + noteValue + "Heading: " + Float.toString(degree) + " degrees");
//
//            } else if (degree > 15 && degree < 25) {
//                // Tilted down so scroll up
//                noteValue = B;
//                noteText.setText("B: " + noteValue);
//
//            } else if (degree > 25 && degree < 35) {
//                // Tilted down so scroll up
//                noteValue = C;
//                noteText.setText("C: " + noteValue);
//
//            } else if (degree > 35 && degree < 40) {
//                // Tilted down so scroll up
//                noteValue = CsDb;
//                noteText.setText("CsDb: " + noteValue);
//
//            } else if (degree > 40 && degree < 50) {
//                // Tilted down so scroll up
//                noteValue = D;
//                noteText.setText("D: " + noteValue);
//
//            } else if (degree > 50 && degree < 55) {
//                // Tilted down so scroll up
//                noteValue = DsEb;
//                noteText.setText("DsEb: " + noteValue);
//
//            } else if (degree > 55 && degree < 65) {
//                // Tilted down so scroll up
//                noteValue = E;
//                noteText.setText("E: " + noteValue);
//
//            } else if (degree > 65 && degree < 75) {
//                // Tilted down so scroll up
//                noteValue = F;
//                noteText.setText("F: " + noteValue);
//
//            } else if (degree > 75 && degree < 80) {
//                // Tilted down so scroll up
//                noteValue = FsGb;
//                noteText.setText("FsGb: " + noteValue);
//
//            } else if (degree > 80 && degree < 90) {
//                // Tilted down so scroll up
//                noteValue = G;
//                noteText.setText("G: " + noteValue);
//
//            } else if (degree > 95 && degree < 100) {
//                // Tilted down so scroll up
//                noteValue = GsAb;
//                noteText.setText("GsAb: " + noteValue);
//            } else {
//                // extra. Can we use this for harmonic change?
//                noteValue = Aoc;
//                noteText.setText("A220: " + noteValue);
//
//            }



            //transplant code. tone
            //isRunning tells us if the audio is playing
            // SETTING UP START BUTTON
            findViewById(R.id.main_start_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // isRunning tells us if the audio is playing
                    if (!isRunning) {
                        audioThread_One = new Thread() {

                            public void run() {
                                // Update isRunning value
                                isRunning = true;
                                // SETTING UP THE AUDIO PARAMS
                                int minimumBufferSize = AudioTrack.getMinBufferSize(
                                        SAMPLE_RATE,
                                        AudioFormat.CHANNEL_OUT_STEREO,
                                        AudioFormat.ENCODING_PCM_16BIT);
                                audioTrack = new AudioTrack(
                                        AudioManager.STREAM_MUSIC,
                                        SAMPLE_RATE,
                                        AudioFormat.CHANNEL_OUT_STEREO,
                                        AudioFormat.ENCODING_PCM_16BIT,
                                        minimumBufferSize,
                                        AudioTrack.MODE_STREAM);//Look into MODE_STATIC decide if it's useful

                                // SOUNDTRACK VARIABLES
                                short[] audioDataSamples = new short[minimumBufferSize];
                                Random rn = new Random();
                                int amplitude = 1000;//rn.nextInt(1000 - 100 + 1) + 100;//tremolo, for consistency add boolean that defaults to 1000 (to set tremolo, turn the min(300) into a variable).
                                double twoPi = 8. * Math.atan(1.);
                                double frequency = notePicker.NotePicker(currentDegree);
                                double phase = 0.0;



                                audioTrack.play();

                                // synthesis loop
                                while (isRunning) {
//                                    frequency = ;
                                    Log.v("notevalue: ", "nv: " + frequency);
                                    for (int i = 0; i < minimumBufferSize; i++) {
                                        audioDataSamples[i] = (short) (amplitude * Math.sin(phase));
                                        phase += twoPi * frequency / SAMPLE_RATE;
                                    }
                                    audioTrack.write(audioDataSamples, 0, minimumBufferSize);
                                }

                            }
                        };
                        audioThread_One.start();

                    }
                } //onClick
            });
            findViewById(R.id.main_stop_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (audioThread_One.isAlive() && !audioThread_One.isInterrupted() && isRunning) {
                        isRunning = false;
                        audioTrack.stop();
                        audioTrack.release();
                        audioThread_One.interrupt();
                        mSensorManager.unregisterListener(Magnetometer.this);

                    }

                }
            });

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // not in use
        }

    }




//

//public class MainActivity extends AppCompatActivity {

//    Thread audioThread_One;
//    Thread audioThread_Two;
//    public static final int SAMPLE_RATE = 44100;
//    boolean isRunning = false;
//    SeekBar seekBar;
//    double seekbarValue;
//    AudioTrack audioTrack;
//    SensorManager sensorManager;
//    double X;
//    double Y;
//    double noteValue;
//    double delayedNoteValue;
//    TextView axisValueTextView; // declare Z axis object
//
//
//    TextView xCoor;
//    TextView yCoor;
//    TextView ZCoor;
//    TextView noteText;
//
//    public static double A = 440.00;   // hz  "Concert A" A above middle C
//    private double AsBb = 466.16;   // hz: A#/Bb
//    private double B = 493.88;   // hz: B
//    private double C = 532.25;   // hz: C one octave higher than middle C
//    private double CsDb = 554.37;   // hz: C#/Db (C sharp/ D flat(minor))
//    private double D = 587.33;
//    private double DsEb = 622.25;
//    private double E = 659.26;
//    private double F = 698.46;
//    private double FsGb = 739.99;
//    private double G = 783.99;
//    private double GsAb = 830.61;
//    private double Aoc=220;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        // SETTING UP VIEWS
//        seekBar = (SeekBar) findViewById(R.id.main_textview);
//        axisValueTextView = (TextView) findViewById(R.id.xCoor);
//
//        noteText = (TextView) findViewById(R.id.note_text);
//
//        xCoor = (TextView) findViewById(R.id.xCoor);
//
//
//
//
//
//        //note oscillator
////        Random randm = new Random();
////
////        D = randm.nextInt(294 - 293 + 1) + 293;     //D4
////        //CsDb = randm.nextInt(277 - 274 + 1) + 274;  //C♯4/D♭4
////        C = randm.nextInt(265 - 255 + 1) + 255;     //middle C
////        B =
//        Random randm = new Random();
//        //Aoc = randm.nextInt(1000 - 20 + 1) + 20;
//
//
//
//
//
//
//// SETTING UP Acceleration
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        sensorManager.registerListener(new SensorEventListener() {
//                                           @Override
//                                           public void onSensorChanged(SensorEvent event) {
//                                               if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                                                   // assign direction Values:
//                                                   // x = 0
//                                                   // y = 1
//                                                   // z = 2
//                                                   X = event.values[0];
//                                                   Y = event.values[1];
//
//
//
//                                                   xCoor.setText("X : " + String.valueOf(X));
//
//                                                   if (X > 9 && 8 < 10) {
//                                                       // Tilted up so scroll down
//                                                       noteValue = A;
//                                                       noteText.setText("A: "+ noteValue);
//
//                                                   } else if (X > 8 && X < 9) {
//                                                       // Tilted down so scroll up
//                                                       noteValue = AsBb;
//                                                       noteText.setText("AsBb: "+ noteValue);
//
//                                                   } else if (X > 7 && X < 8) {
//                                                       // Tilted down so scroll up
//                                                       noteValue = B;
//                                                       noteText.setText("B: "+ noteValue);
//
//                                                   } else if (X > 6 && X < 7) {
//                                                       // Tilted down so scroll up
//                                                       noteValue = C;
//                                                       noteText.setText("C: "+ noteValue);
//
//                                                   } else if (X > 5 && X < 6) {
//                                                       // Tilted down so scroll up
//                                                       noteValue = CsDb;
//                                                       noteText.setText("CsDb: "+ noteValue);
//
//                                                   } else if (X > 4 && X < 5) {
//                                                       // Tilted down so scroll up
//                                                       noteValue = D;
//                                                       noteText.setText("D: "+ noteValue);
//
//                                                   } else if (X > 3 && X < 4) {
//                                                       // Tilted down so scroll up
//                                                       noteValue = DsEb;
//                                                       noteText.setText("DsEb: "+ noteValue);
//
//                                                   } else if (X > 2 && X < 3) {
//                                                       // Tilted down so scroll up
//                                                       noteValue = E;
//                                                       noteText.setText("E: "+ noteValue);
//
//                                                   } else if (X > 1 && X < 2) {
//                                                       // Tilted down so scroll up
//                                                       noteValue = F;
//                                                       noteText.setText("F: "+ noteValue);
//
//                                                   } else if (X > 0 && X < 1) {
//                                                       // Tilted down so scroll up
//                                                       noteValue = FsGb;
//                                                       noteText.setText("FsGb: "+ noteValue);
//
//                                                   } else if (X > -1 && X < 0) {
//                                                       // Tilted down so scroll up
//                                                       noteValue = G;
//                                                       noteText.setText("G: "+ noteValue);
//
//                                                   } else if (X > -2 && X < -1) {
//                                                       // Tilted down so scroll up
//                                                       noteValue = GsAb;
//                                                       noteText.setText("GsAb: "+ noteValue);
//                                                   } else {
//                                                       // Stop scrolling
//                                                       noteValue = Aoc;
//                                                       noteText.setText("A220: "+ noteValue);
//
//                                                   }
//                                               }
//                                           }
//
//                                           @Override
//                                           public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//
//                                           }
//                                       },
//                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
//
//
//        // SETTING UP START BUTTON
//        findViewById(R.id.main_start_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // isRunning tells us if the audio is playing
//                if (!isRunning) {
//                    audioThread_One = new Thread() {
//                        public void run() {
//                            // Update isRunning value
//                            isRunning = true;
//                            // SETTING UP THE AUDIO PARAMS
//                            int minimumBufferSize = AudioTrack.getMinBufferSize(
//                                    SAMPLE_RATE,
//                                    AudioFormat.CHANNEL_OUT_MONO,
//                                    AudioFormat.ENCODING_PCM_16BIT);
//                            audioTrack = new AudioTrack(
//                                    AudioManager.STREAM_MUSIC,
//                                    SAMPLE_RATE,
//                                    AudioFormat.CHANNEL_OUT_MONO,
//                                    AudioFormat.ENCODING_PCM_16BIT,
//                                    minimumBufferSize,
//                                    AudioTrack.MODE_STREAM);
//
//                            // SOUNDTRACK VARIABLES
//                            short[] audioDataSamples = new short[minimumBufferSize];
//                            Random rn = new Random();
//                            int amplitude = 1000;//rn.nextInt(1000 - 100 + 1) + 100;//tremolo, for consistency add boolean that defaults to 1000 (to set tremolo, turn the min(300) into a variable).
//                            double twoPi = 8. * Math.atan(1.);
//                            double frequency = 440.f;
//                            double phase = 0.0;
//
//                            audioTrack.play();
//
//                            // synthesis loop
//                            while (isRunning) {
//                                frequency = noteValue;
//                                for (int i = 0; i < minimumBufferSize; i++) {
//                                    audioDataSamples[i] = (short) (amplitude * Math.sin(phase));
//                                    phase += twoPi * frequency / SAMPLE_RATE;
//                                }
//                                audioTrack.write(audioDataSamples, 0, minimumBufferSize);
//                            }
//
//                        }
//                    };
//                    audioThread_One.start();
//
//                }
//
//
//
//
//                if (!isRunning) {
//                    audioThread_Two = new Thread() {
//                        public void run() {
//                            // Update isRunning value
//                            isRunning = true;
//                            // SETTING UP THE AUDIO PARAMS
//                            int minimumBufferSize = AudioTrack.getMinBufferSize(
//                                    SAMPLE_RATE,
//                                    AudioFormat.CHANNEL_OUT_MONO,
//                                    AudioFormat.ENCODING_PCM_16BIT);
//                            audioTrack = new AudioTrack(
//                                    AudioManager.STREAM_MUSIC,
//                                    SAMPLE_RATE,
//                                    AudioFormat.CHANNEL_OUT_MONO,
//                                    AudioFormat.ENCODING_PCM_16BIT,
//                                    minimumBufferSize,
//                                    AudioTrack.MODE_STREAM);
//
//                            // SOUNDTRACK VARIABLES
//                            short[] audioDataSamples = new short[minimumBufferSize];
//                            Random rn = new Random();
//                            int amplitude = 1000;//rn.nextInt(1000 - 100 + 1) + 100;//tremolo, for consistency add boolean that defaults to 1000 (to set tremolo, turn the min(300) into a variable).
//                            double twoPi = 8. * Math.atan(1.);
//                            double frequency = 440.f;
//                            double phase = 1.0;
//
//                            audioTrack.play();
//
//                            // synthesis loop
//                            while (isRunning) {
//                                frequency = A;
//                                for (int i = 0; i < minimumBufferSize; i++) {
//                                    audioDataSamples[i] = (short) (amplitude * Math.sin(phase));
//                                    phase += twoPi * frequency / SAMPLE_RATE;
//                                }
//                                audioTrack.write(audioDataSamples, 0, minimumBufferSize);
//                            }
//
//                        }
//                    };
//                    audioThread_Two.start();
//
//                }
//
//
//
//            } //onClick
//        });
//
//
//        findViewById(R.id.main_stop_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (audioThread_One.isAlive() && !audioThread_One.isInterrupted() && isRunning) {
//                    isRunning = false;
//                    audioTrack.stop();
//                    audioTrack.release();
//                    audioThread_One.interrupt();
//
//                }
//                if (audioThread_Two.isAlive() && !audioThread_Two.isInterrupted() && isRunning) {
//                    isRunning = false;
//                    audioTrack.stop();
//                    audioTrack.release();
//                    audioThread_Two.interrupt();
//                }
//            }
//        });
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser) {
//                    seekbarValue = progress * 0.1;
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//        // start a new audioThread to synthesise audio
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    public void onDestroy() {
//        super.onDestroy();
//        isRunning = false;
//        try {
//            audioThread_One.join();
//            audioThread_Two.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        audioThread_Two = null;
//        audioThread_One = null;
//    }
//
//
//}

//}
