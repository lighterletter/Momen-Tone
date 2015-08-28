package lighterletter.c4q.nyc.momentone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView =  (ImageView)findViewById(R.id.imageView);
        final PlayPauseDrawable mPlayPauseDrawable = new PlayPauseDrawable(60, 0XFF101840, 0XFFffffff,300);
        imageView.setImageDrawable(mPlayPauseDrawable);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayPauseDrawable.toggle();
            }
        });
    }
}
