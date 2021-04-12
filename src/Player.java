import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.Vector;

/**
 * An utility class for playing back audio files using Java Sound API.
 * @author www.codejava.net
 *
 */
public class Player implements LineListener {
    private static final int SECONDS_IN_HOUR = 60 * 60;
    private static final int SECONDS_IN_MINUTE = 60;

    private boolean playCompleted;
    private boolean isStopped;
    private boolean isPaused;

    private Vector<Music> playlist;

    public void load(Vector<Music> playlist) {
        this.playlist = playlist;
    }

    public int getClipSecondLength(int i) {
        return this.playlist.get(i).getDuration();
    }

    public String getClipLengthString(int i) {
        String length = "";
        long hour = 0;
        long minute = 0;
        long seconds = this.playlist.get(i).getDuration();

        System.out.println(seconds);

        if (seconds >= SECONDS_IN_HOUR) {
            hour = seconds / SECONDS_IN_HOUR;
            length = String.format("%02d:", hour);
        } else {
            length += "00:";
        }

        minute = seconds - hour * SECONDS_IN_HOUR;
        if (minute >= SECONDS_IN_MINUTE) {
            minute = minute / SECONDS_IN_MINUTE;
            length += String.format("%02d:", minute);

        } else {
            minute = 0;
            length += "00:";
        }

        long second = seconds - hour * SECONDS_IN_HOUR - minute * SECONDS_IN_MINUTE;

        length += String.format("%02d", second);

        return length;
    }

    void play(Timer timer) throws IOException {
        for (int i = 0; i < this.playlist.size(); i++) {
            timer.run(this.playlist.get(i));
            System.out.println(this.playlist.get(i).getName());

        }
    }

    /**
     * Stop playing back.
     */
    public void stop() {
        isStopped = true;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    /**
     * Listens to the audio line events to know when the playback completes.
     */
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();

        if (type == LineEvent.Type.STOP) {
            System.out.println("STOP EVENT");
            if (isStopped || !isPaused) {
                playCompleted = true;
            }
        }
    }

    public Music getAudioClip(int i) {
        return this.playlist.get(i);
    }
}