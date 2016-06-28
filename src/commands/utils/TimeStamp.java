package commands.utils;

/**
 * Created by jackbachman on 6/28/16.
 */
public class TimeStamp {
    private double startTime;
    private double endTime;

    public TimeStamp(double duration) {
        startTime = System.currentTimeMillis();
        endTime = startTime + duration;
    }

    public Double getEndTime() {
        return endTime;
    }
}
