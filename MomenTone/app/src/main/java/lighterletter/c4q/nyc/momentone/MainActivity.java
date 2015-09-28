package lighterletter.c4q.nyc.momentone;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //represents the instance of the custom VIew that we added to the layout.
    DrawingView drawView;
    //main function buttons for the palette
    ImageView currPaint, drawBtn, eraseBtn, newBtn, saveBtn, shareBtn;

    // layout that contains button to retrieve the first paint color in the palette.
    LinearLayout paintLayout;
    //Brush size: To store three dimension values defined in dimens,
    // TODO: transfer into a seekbar or something similar
    private float smallBrush, mediumBrush, largeBrush, xlargeBrush;


    //sensor manager passed to sensor class.

    //synth
    SoundGen soundgen;

    Toolbar toolbar;
    NavigationView mColorNavigationView;
    NavigationView mToolNavigationView;
    DrawerLayout mDrawerLayout;
    ImageView swipeRight;
    ImageView swipeLeft;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundgen = new SoundGen();


        //draw functionality:
        drawView = (DrawingView) findViewById(R.id.canvas);
        paintLayout = (LinearLayout) findViewById(R.id.paint_colors);

        //get the first button and store it as the instance variable:
        currPaint = (ImageView) paintLayout.getChildAt(0);
        // sets alternate options for when the button is pressed.
        currPaint.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.paint_pressed));
        //size
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
        xlargeBrush = getResources().getInteger(R.integer.xlarge_size);

        //add another for the drawing button.
        drawBtn = (ImageView) findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        // default brush size
        drawView.setBrushSize(mediumBrush);

        //erase button
        eraseBtn = (ImageView) findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        //new canvas button
        newBtn = (ImageView) findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        //save drawing button
        saveBtn = (ImageView) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        //share button
        shareBtn = (ImageView) findViewById(R.id.share_btn);
        shareBtn.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        mColorNavigationView = (NavigationView) findViewById(R.id.color_drawer);
        mToolNavigationView = (NavigationView) findViewById(R.id.tool_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        swipeRight = (ImageView) findViewById(R.id.colors);
        swipeLeft = (ImageView) findViewById(R.id.tools);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        swipeRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                    mDrawerLayout.openDrawer(mColorNavigationView);
                }

            }
        });

        swipeLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                    mDrawerLayout.openDrawer(mToolNavigationView);
                }
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //listener for draw functionality buttons
    public void onClick(View view) {
        //respond to clicks
        Log.v("Somethings been clicked", "onClick is called");
        if (view.getId() == R.id.draw_btn) {
            Log.v("Somethings been clicked", "draw button pressed");

            //creates Dialogue and sets title for brush size when brush (draw_btn) is clicked.

            //display a dialog presenting the user with the three button sizes. (Find better way of implementing this.) layout defined in res/layouts
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size: ");
            //set the layout (contentView into Dialogue object)
            brushDialog.setContentView(R.layout.brush_size_picker_dialoge_layout);
            //Listen for clicks on the three size buttons, starting with the small one:
            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                    Log.v("Somethings been clicked", "small brush selected");
                }
            });
            //We set the size using the methods we added to the custom View class as soon as the
            // user clicks a brush size button, then immediately dismiss the Dialog.
            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                    Log.v("Somethings been clicked", "medium brush selected");
                }
            });
            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();

                    Log.v("Somethings been clicked", "large brush selected");
                }
            });
            ImageButton xlargeBtn = (ImageButton) brushDialog.findViewById(R.id.xlarge_brush);
            xlargeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(xlargeBrush);
                    drawView.setLastBrushSize(xlargeBrush);
                    brushDialog.dismiss();

                    Log.v("Somethings been clicked", "large brush selected");
                }
            });
            //complete draw button section of Onclick by dysplaying Dialog:
            brushDialog.show();

        } //ERASER SIZE
        else if (view.getId() == R.id.erase_btn) {

            //switch to erase - choose eraser size
            Log.v("Somethings been clicked", " erase is clicked!");
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Erase size");
            brushDialog.setContentView(R.layout.brush_size_picker_dialoge_layout);
            //small
            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                    Log.v("Somethings been clicked", "small erase selected");
                }
            });
            //medium
            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                    Log.v("Somethings been clicked", "medium erase selected");
                }
            });
            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                    Log.v("Somethings been clicked", "large erase selected");
                }
            });
            ImageButton xlargeBtn = (ImageButton) brushDialog.findViewById(R.id.xlarge_brush);
            xlargeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(xlargeBrush);
                    brushDialog.dismiss();
                    Log.v("Somethings been clicked", "large erase selected");
                }
            });
            brushDialog.show();


        } //NEW DRAWING BUTTON
        else if (view.getId() == R.id.new_btn) {

            //Assert message to confirm user's actions
            Log.v("Somethings been clicked", "new canvas is clicked!");
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing? Current masterpiece will be lost.");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                    Log.v("Somethings been clicked", "new drawing dialog check");
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Log.v("Somethings been clicked", "no new, dialog cancelled");
                }
            });
            newDialog.show();


        } //SAVE BUTTON
        else if (view.getId() == R.id.save_btn) {

            //save drawing to gallery
            Log.v("Somethings been clicked", "save button clicked");
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {//could do with more quirky
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //save drawing
                    //The method returns the URL of the image created, or null if the operation was unsuccessful
                    drawView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(getContentResolver(),
                            drawView.getDrawingCache(), UUID.randomUUID().toString() + ".jpeg", "Masterpiece");

                    // - this lets us give user feedback:
                    if (imgSaved != null) {
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Masterpiece saved to gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    } else {
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved :(", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    //Destroys the drawing cache so that any future drawings saved wont use the existing cache:
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();

        } //SHARE BUTTON
        else if (view.getId() == R.id.share_btn) {
            drawView.setDrawingCacheEnabled(true);

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            drawView.getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());

                Uri photoUri = Uri.fromFile(f);
                share.putExtra(Intent.EXTRA_STREAM, photoUri);
                startActivity(Intent.createChooser(share, "Share Image"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            drawView.destroyDrawingCache();
        }
    }

    //Color Picker
    public void paintClicked(View view) {
        drawView.setErase(false); // assumes user wants to draw if paint is clicked
        drawView.setBrushSize(drawView.getLastBrushSize()); // gets the last brush size used.
        // TODO: Think about user experience and usability.
        //use chosen color
        if (view != currPaint) {
            //update color
            ImageButton imgView = (ImageButton) view;
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
