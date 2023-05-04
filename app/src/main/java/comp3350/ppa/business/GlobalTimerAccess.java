package comp3350.ppa.business;

public interface GlobalTimerAccess {
    void setSeconds(int seconds);
    int getSecondsRemaining();
    int getSecondsRemaining(long timestamp);
    String getRelativeTimeString();
    String getRelativeTimeString(long timestamp);
    void start();
    void start(long timestamp);
    void stop();
    boolean isRunning();
}
