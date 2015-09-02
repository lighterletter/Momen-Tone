package lighterletter.c4q.nyc.momentone;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.LinearLayout;

import lighterletter.c4q.nyc.momentone.Views.DrawingView;


public class MainActivity extends AppCompatActivity {

    //represents the instance of the custom VIew that we added to the layout.
     DrawingView drawView;
    //paint color button in the pallete
     ImageButton currPaint;
    //need layout that contains button to retrieve the first paint color in the palette.
    LinearLayout paintLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawingView) findViewById(R.id.drawing);
        paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        //get the first button and store it as the instance variable:
        currPaint = (ImageButton) paintLayout.getChildAt(0);

        // sets alternate options for when the button is pressed.
        currPaint.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.paint_pressed));


        ImageView imageView =  (ImageView)findViewById(R.id.imageView);
        final PlayPauseDrawable mPlayPauseDrawable = new PlayPauseDrawable(60, 0XFF101840, 0XFFffffff,300);
        imageView.setImageDrawable(mPlayPauseDrawable);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayPauseDrawable.toggle();

                Intent i = new Intent(getApplicationContext(), stepCounter.class);
                startActivity(i);

                Intent j = new Intent(getApplicationContext(), DrawingView.class);
                startActivity(j);

            }
        });
    }
    public void paintClicked(View view){
        //use chosen color
        if (view != currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            //after receiving the color tag call setColor method in DrawingView  on the custom drawing View Object:
            drawView.setColor(color);
            //update UI to reflect chosen color and reset previous one
            imgView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.paint_pressed));
            currPaint.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.paint));
            currPaint = (ImageButton) view;
        }
    }

}
