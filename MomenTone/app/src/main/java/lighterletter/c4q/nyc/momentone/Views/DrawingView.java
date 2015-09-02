package lighterletter.c4q.nyc.momentone.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by c4q-john on 9/2/15.
 * reference from: http://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-interface-creation--mobile-19021
 */

//custom view made to behave as a drawing view: must override needed methods.
public class DrawingView extends View {

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


    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        //set up area for interaction

        //instantiate to set up for draw
        drawPath = new Path();
        drawPaint = new Paint();
        //initial color
        drawPaint.setColor(paintColor);
        //initial path properties
        drawPaint.setAntiAlias(true); //this, stroke join and cap styles make the drawing appear smoother.
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        //instantiate the canvas paint object
        //set dithering by passing a parameter to the constructor
        canvasPaint = new Paint(Paint.DITHER_FLAG);

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
                drawPath.moveTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath,drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate(); // Calling this will invalidate the view and will cause the onDraw method to execute.
        return true;
    }

    public void setColor(String newColor){
        //set color

        //start by invalidating the View
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

}
