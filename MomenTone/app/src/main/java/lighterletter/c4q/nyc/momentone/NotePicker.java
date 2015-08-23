package lighterletter.c4q.nyc.momentone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by c4q-john on 8/21/15.
 */
public class NotePicker extends AppCompatActivity{


    double noteValue;



    public static double A = 440.00;   // hz  "Concert A" A above middle C
    double AsBb = 466.16;   // hz: A#/Bb
    double B = 493.88;   // hz: B
    double C = 532.25;   // hz: C one octave higher than middle C
    double CsDb = 554.37;   // hz: C#/Db (C sharp/ D flat(minor))
    double D = 587.33;
    double DsEb = 622.25;
    double E = 659.26;
    double F = 698.46;
    double FsGb = 739.99;
    double G = 783.99;
    double GsAb = 830.61;
    double Aoc = 220;




    public double NotePicker(double degree) {
        double[] notes = { };
        if (degree > 100 && degree < 95) {
            noteValue = noteValue +A;
            Log.v("notevalue: ", "nv: "+ noteValue);
            return noteValue;
        } else if (degree > 80 && degree < 90) {
            noteValue = noteValue + AsBb;
            Log.v("notevalue: ", "nv: "+ noteValue);
            return noteValue;
        } else if (degree > 75 && degree < 80) {
            noteValue = noteValue + B;
            Log.v("notevalue: ", "nv: "+ noteValue);
            return noteValue;
        } else if (degree > 65 && degree < 75) {
            noteValue = noteValue + C;
            return noteValue;
        } else if (degree > 55 && degree < 65) {
            noteValue = CsDb;
            return noteValue;
        } else if (degree > 50 && degree < 55) {
            noteValue = D;
            return noteValue;
        } else if (degree > 40 && degree < 50 ) {
            noteValue = DsEb;
            return noteValue;
        } else if (degree > 35 && degree < 40) {
            noteValue = E;
            return noteValue;
        } else if (degree > 25 && degree < 35) {
            noteValue = F;
            return noteValue;
        } else if (degree > 15 && degree < 25) {
            noteValue = FsGb;
            return noteValue;
        } else if (degree > 10 && degree < 15) {
            noteValue = G;
            return noteValue;
        } else if (degree > 1 && degree < 10) {
            noteValue = GsAb;
            return noteValue;
        } else {
            // extra. Can we use this for harmonic change?
            return noteValue;
        }

    }

}
