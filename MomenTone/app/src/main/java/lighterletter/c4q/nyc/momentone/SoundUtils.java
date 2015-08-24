package lighterletter.c4q.nyc.momentone;

/**
 * Created by gsyrimis on 8/23/15.
 */
public class SoundUtils {
    public void soundIs(){
        double sampleRate = 44100.0;
        double frequency = 440.0;
        double amplitude = 0.8;
        double seconds = 2.0;
        double twoPiF = 2 * Math.PI * frequency;
        float[] buffer = new float[(int) (seconds * sampleRate)];
        for (int sample = 0; sample < buffer.length; sample++) {
            double time = sample / sampleRate;
//            buffer[sample] = (float) amplitude * Math.sin(twoPiF * time);
        }
    }
}
