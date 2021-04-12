import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.sound.sampled.Clip;
import javax.swing.JLabel;
import javax.swing.JSlider;

/**
 * This class counts playing time in the form of HH:mm:ss
 * It also updates the time slider
 * @author www.codejava.net
 *
 */
public class Timer extends Thread {

    private static final int SECONDS_IN_HOUR = 60 * 60;
    private static final int SECONDS_IN_MINUTE = 60;

    private DateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");
    private boolean isRunning = false;
    private boolean isPause = false;
    private boolean isReset = false;
    private long startTime;
    private long pauseTime;
    private int pause;

    private JLabel labelRecordTime;
    private JSlider slider;
    private JLabel musicNameLabel;
    private JLabel fullTime;

    private Music music;
    private int duration;

    public void setAudioClip(Music music) {
        this.music = music;
    }

    public Timer(JLabel labelRecordTime, JSlider slider, JLabel musicNameLabel, JLabel fullTime) {
        this.musicNameLabel = musicNameLabel;
        this.fullTime = fullTime;
        this.labelRecordTime = labelRecordTime;
        this.slider = slider;
        this.pauseTime = 0;
    }

    public void run(Music music) {
        setAudioClip(music);

        isRunning = true;

        musicNameLabel.setText("Playing now: " + music.getName());
        slider.setMaximum(music.getDuration());
        fullTime.setText(getClipLengthString(music.getDuration()));

        int time = music.getDuration();
        int cont = 0;
        pause = 0;
        while (isRunning) {
            try {
                Thread.sleep(1000);
                time -= 1;
                if (!isPause) {
                    time += pause;
                    pause = 0;
                    if (time > 0) {
                        cont += 1;
                        labelRecordTime.setText(getClipLengthString(cont));
                        slider.setValue(cont);
                    } else {
                        labelRecordTime.setText("00:00:00");
                        cont = 0;
                        slider.setValue(cont);
                        isRunning = false;
                    }
                } else {
                    pause += 1;
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                if (isReset && !isPause) {
                    slider.setValue(0);
                    labelRecordTime.setText("00:00:00");
                    isRunning = false;
                    break;
                }
            }
        }
    }


    /**
     * Reset counting to "00:00:00"
     */
    void reset() {
        isReset = true;
        isRunning = false;
    }

    void pauseTimer() {
        isPause = true;
    }

    void resumeTimer() {
        isPause = false;
    }

    /**
     * Generate a String for time counter in the format of "HH:mm:ss"
     * @return the time counter
     */

    private String getClipLengthString(int secondsMusic) {
        String length = "";
        long hour = 0;
        long minute = 0;
        long seconds = secondsMusic;

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
}