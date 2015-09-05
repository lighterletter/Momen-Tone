package lighterletter.c4q.nyc.momentone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by c4q-john on 9/2/15.
 * referenced from: http://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-interface-creation--mobile-19021
 *
 * This class connects with the main activity to handle the drawing features for the app.
 * Contains code that maps user input to pitch and volume control.
 */

//custom view made to behave as a drawing view: must override needed methods. (custom drawing view class)
public class DrawingView extends View {

    // Reference to class that handles sound.
    MTherory synth;

    //drawing path: traces drawing action on the canvas. Both canvas and drawing are represented by Paint Objects.
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000; // corresponds to the initial paint color in the palette we created.
    //canvas
    private Canvas drawCanvas;  //drawpaint will be drawn onto the canvas which is drawn with canvasPaint
    //canvas bitmap
    private Bitmap canvasBitmap;

    //Alters class to use different brush sizes:
    private float brushSize, lastBrushSize;
    // We will use the first variable for the brush size and the second to keep track of the last
    // brush size used when the user switches to the eraser, so that we can revert back to the
    // correct size when they decide to switch back to drawing.

    //eraser boolean
    private boolean erase = false;
    public void setErase(boolean isErase){
        //set erase true or false
        //update the flag variable
        erase = isErase;
        if (erase) {
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));//look up porter mode
            //Also PorterDuff.Mode options in the android docs for a more advanced topic to research
        }
        else{
            drawPaint.setXfermode(null);
        }
    }


    // Class Constructor
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
        synth = new MTherory();
    }

    private void setupDrawing() {
        //set up area for interaction
        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;

        //instantiate to set up for draw
        drawPath = new Path();
        drawPaint = new Paint();
        //initial color
        drawPaint.setColor(paintColor);
        //initial path properties
        drawPaint.setAntiAlias(true); //this, stroke join and cap styles make the drawing appear smoother.
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        //instantiate the canvas paint object
        //set dithering by passing a parameter to the constructor
        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }

    //next three methods are called by the Main activity
    public void setBrushSize(float newSize){
        //update brush size
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth(brushSize); //passing values from dimens file. Could be turned into a seekbar.
    }

    public void setLastBrushSize(float lastSize){
         lastBrushSize = lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        //instantiate the drawing canvas and bitmap using the width and height values
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    //makes it a custom drawing view
    @Override
    protected void onDraw(Canvas canvas) {
        //draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
        // The abilitity for the user to draw the path using the drawing paint is not implemented yet
        // each time the user draws using touch interaction we will invalidate the view causing the
        // onDraw method to execute.
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //detect user touch
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //Moves pointer to:
                drawPath.moveTo(touchX,touchY);

                //synth
//                synth.play_one = true;
//                synth.fr_1 = event.getX(); //pitch
//                synth.amp = (int)event.getY();//volume
//                Log.v("FREQUENCY", "" + synth.fr_1);
                break;

            case MotionEvent.ACTION_MOVE:
                //draw: creates path
                drawPath.lineTo(touchX, touchY);

                //synth: changes pitch
                synth.play_one = true;
                synth.fr_1 =  event.getX();
                synth.amp = (int) event.getY();//
                Log.v("FREQUENCY", "" + synth.fr_1);
                break;

            case MotionEvent.ACTION_UP:
                //draw: sets path.
                drawCanvas.drawPath(drawPath,drawPaint);
                drawPath.reset();

                //synth: closes channel: (There must be a better to to do this)


                synth.play_all = false;
                synth.play_one = false;
                break;

            // To be tested, not sure what this does yet but I put it here because it was in the code
            // for the onTouch. Must test.
//            case MotionEvent.ACTION_CANCEL:
//                break;

            default:
                return false;

        }

        invalidate(); // Calling this will invalidate the view and will cause the onDraw method to execute.
        return true;
    }


    //TODO: Create some way to do a color fill.
    public void setColor(String newColor){
        //set color

        //start by invalidating the View
        invalidate();

        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }


    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate(); //clears the canvas and updates the display.
    }


    //This was the code used before for finger synthesis in it's own dedicated class.
    // I put it here for reference and legacy purposes.
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction();
//        switch (action)
//        {
//            case MotionEvent.ACTION_DOWN:
//
//                synth.play_one = true;
//                synth.fr_1 = event.getX(); //pitch
//                synth.amp = (int)event.getY();//volume
//                Log.v("FREQUENCY", "" + synth.fr_1);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                synth.play_all = true;
//                synth.fr_1 = event.getX();
//                synth.amp = (int) event.getY();//
//                Log.v("FREQUENCY", "" + synth.fr_1);
//                break;
//            case MotionEvent.ACTION_UP:
//                synth.play_all = false;
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
}
