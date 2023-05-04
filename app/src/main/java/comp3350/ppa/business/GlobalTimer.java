package comp3350.ppa.business;

import java.util.Calendar;
import java.util.Locale;

public class GlobalTimer implements GlobalTimerAccess {
    private int timerLengthSeconds;
    private long endTimeStamp;
    private boolean timerRunning;

    public void setSeconds(int seconds) {
        if (seconds < 0)
            throw new IllegalArgumentException("Seconds must be positive");
        timerLengthSeconds = seconds;
    }

    public int getSecondsRemaining() {
        return getSecondsRemaining(Calendar.getInstance().getTimeInMillis());
    }

    public int getSecondsRemaining(long timestamp) {
        if (timestamp < 0)
            throw new IllegalArgumentException("Timestamp must be positive");
        if (!timerRunning)
            return timerLengthSeconds;
        return (int) Math.max(0, (endTimeStamp - timestamp) / 1000);
    }

    public String getRelativeTimeString() {
        return getRelativeTimeString(Calendar.getInstance().getTimeInMillis());
    }

    public String getRelativeTimeString(long timestamp) {
        int fullSeconds = getSecondsRemaining(timestamp);
        int minutes = Math.floorDiv(fullSeconds, 60);
        int seconds = fullSeconds % 60;
        return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
    }

    public void start() {
        start(Calendar.getInstance().getTimeInMillis());
    }

    public void start(long timestamp) {
        if (this.timerLengthSeconds == 0)
            throw new IllegalArgumentException("Cannot start a timer with length of 0 seconds");
        if (timestamp < 0)
            throw new IllegalArgumentException("Timestamp must be positive");
        timerRunning = true;
        endTimeStamp = timestamp + (timerLengthSeconds * 1000L);
    }

    public void stop() {
        timerRunning = false;
    }

    public boolean isRunning() {
        return timerRunning;
    }
}
