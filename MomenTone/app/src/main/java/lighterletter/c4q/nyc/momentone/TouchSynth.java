package lighterletter.c4q.nyc.momentone;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by c4q-john on 8/22/15.
 */
public class TouchSynth extends Activity implements View.OnTouchListener {

    SineThread trackOne;
    SquareThread trackTwo;

    Button startBTN;
    Button stopBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_synth);
        SensorUtils sensorUtils = new SensorUtils(getApplicationContext());
        startBTN = (Button) findViewById(R.id.touch_start_btn);
        startBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!trackOne.isRunning()) {
                    trackOne.start();
                }
                if (!trackTwo.isRunning()) {
                    trackTwo.start();
                }

            }
        });
        stopBTN = (Button) findViewById(R.id.touch_stop_btn);
        stopBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trackOne.isRunning()) {
                    trackOne.kill();
                }
                if (trackTwo.isRunning()) {
                    trackTwo.kill();
                }

            }
        });
        View mainView = this.findViewById(R.id.touch_main_view);
        mainView.setOnTouchListener(this);
        // CREATE THREAD AND SOUND
        trackOne = new SineThread();
        trackTwo = new SquareThread();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {   // on down touch
            case MotionEvent.ACTION_DOWN:
                trackOne.setAmplitude((int) event.getY());//volume
                trackTwo.setAmplitude((int) event.getY());//volume
                trackOne.setFrequency(event.getX()); //pitch
                trackTwo.setFrequency(event.getX()); //pitch
                break;
            case MotionEvent.ACTION_MOVE:
                trackOne.setAmplitude((int) event.getY());//volume
                trackTwo.setAmplitude((int) event.getY());//volume
                trackOne.setFrequency(event.getX()); //pitch
                trackTwo.setFrequency(event.getX()); //pitch

                break;
            case MotionEvent.ACTION_UP:
                trackOne.setAmplitude(0);
                trackTwo.setAmplitude(0);
                trackOne.setFrequency(0);
                trackTwo.setFrequency(0);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }


}
