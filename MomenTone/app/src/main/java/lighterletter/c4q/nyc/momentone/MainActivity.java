package lighterletter.c4q.nyc.momentone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void accelerometerActivity(View v){
        Intent i = new Intent(this, TouchSynth.class);
        startActivity(i);
    }

}
