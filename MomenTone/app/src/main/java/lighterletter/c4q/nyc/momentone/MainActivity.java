package lighterletter.c4q.nyc.momentone;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Thread audioThread;
    Thread sensorThread;
    public static final int SAMPLE_RATE = 44100;
    boolean isRunning = false;
    SeekBar seekBar;
    double seekbarValue;
    AudioTrack audioTrack;
    SensorManager sensorManager;
    double sensorAxis;
    double noteValue;
    double delayedNoteValue;
    TextView axisValueTextView; // declare Z axis object

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
    private double Aoc  = 880.00;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // SETTING UP VIEWS
        seekBar = (SeekBar) findViewById(R.id.main_textview);
        axisValueTextView = (TextView) findViewById(R.id.xcoor);

        sensorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        delayedNoteValue = noteValue;
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sensorThread.start();

// SETTING UP SENSOR
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(new SensorEventListener() {
                                           @Override
                                           public void onSensorChanged(SensorEvent event) {
                                               if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                                                   // assign directions
                                                   sensorAxis = event.values[0];

                                                   axisValueTextView.setText(String.valueOf(sensorAxis));

                                                   if (sensorAxis > 9 && sensorAxis < 10) {
                                                       // Tilted up so scroll down
                                                       noteValue = A;
                                                   } else if (sensorAxis > 8 && sensorAxis < 9) {
                                                       // Tilted down so scroll up
                                                       noteValue = AsBb;
                                                   } else if (sensorAxis > 7 && sensorAxis < 8) {
                                                       // Tilted down so scroll up
                                                       noteValue = B;
                                                   } else if (sensorAxis > 6 && sensorAxis < 7) {
                                                       // Tilted down so scroll up
                                                       noteValue = C;
                                                   } else if (sensorAxis > 5 && sensorAxis < 6) {
                                                       // Tilted down so scroll up
                                                       noteValue = CsDb;
                                                   } else if (sensorAxis > 4 && sensorAxis < 5) {
                                                       // Tilted down so scroll up
                                                       noteValue = D;
                                                   } else if (sensorAxis > 3 && sensorAxis < 4) {
                                                       // Tilted down so scroll up
                                                       noteValue = DsEb;
                                                   } else if (sensorAxis > 2 && sensorAxis < 3) {
                                                       // Tilted down so scroll up
                                                       noteValue = E;
                                                   } else if (sensorAxis > 1 && sensorAxis < 2) {
                                                       // Tilted down so scroll up
                                                       noteValue = F;
                                                   } else if (sensorAxis > 0 && sensorAxis < 1) {
                                                       // Tilted down so scroll up
                                                       noteValue = FsGb;
                                                   } else if (sensorAxis > -1 && sensorAxis < 0) {
                                                       // Tilted down so scroll up
                                                       noteValue = G;
                                                   } else if (sensorAxis > -2 && sensorAxis < -1) {
                                                       // Tilted down so scroll up
                                                       noteValue = GsAb;
                                                   } else {
                                                       // Stop scrolling
                                                       noteValue = Aoc;
                                                   }
                                               }
                                           }
                                           @Override
                                           public void onAccuracyChanged(Sensor sensor, int accuracy) {
                                           }
                                       },
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);



        // SETTING UP START BUTTON
        findViewById(R.id.main_start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // isRunning tells us if the audio is playing
                if(!isRunning) {
                    audioThread = new Thread() {
                        public void run() {
                            // Update isRunning value
                            isRunning = true;
                            // SETTING UP THE AUDIO PARAMS
                            int minimumBufferSize = AudioTrack.getMinBufferSize(
                                    SAMPLE_RATE,
                                    AudioFormat.CHANNEL_OUT_MONO,
                                    AudioFormat.ENCODING_PCM_16BIT);
                            audioTrack = new AudioTrack(
                                    AudioManager.STREAM_MUSIC,
                                    SAMPLE_RATE,
                                    AudioFormat.CHANNEL_OUT_MONO,
                                    AudioFormat.ENCODING_PCM_16BIT,
                                    minimumBufferSize,
                                    AudioTrack.MODE_STREAM);

                            // SOUNDTRACK VARIABLES
                            short[] audioDataSamples = new short[minimumBufferSize];
                            int amplitude = 10000;
                            double twoPi = 8. * Math.atan(1.);
                            double frequency = 440.f;
                            double phase = 0.0;

                            audioTrack.play();

                            // synthesis loop
                            while (isRunning) {
//                                frequency = noteValue;
                                frequency = delayedNoteValue;
                                for (int i = 0; i < minimumBufferSize; i++) {
                                    audioDataSamples[i] = (short) (amplitude * Math.sin(phase));
                                    phase += twoPi * frequency / SAMPLE_RATE;
                                }
                                audioTrack.write(audioDataSamples, 0, minimumBufferSize);
                            }

                        }
                    };
                    audioThread.start();
                }
            }
        });


        findViewById(R.id.main_stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(audioThread.isAlive()&&!audioThread.isInterrupted()&&isRunning) {
                    isRunning = false;
                    audioTrack.stop();
                    audioTrack.release();
                    audioThread.interrupt();
//                    noteValue =A;
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekbarValue = progress * 0.1;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // start a new audioThread to synthesise audio


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        try {
            audioThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audioThread = null;
    }



}
