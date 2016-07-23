package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class BotLogger {
    public static String mediumTimestamp = "[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "]";
    public static final String LOGGING = " [Logging] ";
    public static final String ERROR = " [Error] ";
    public static final String DANGER = " [DANGER] ";
    public static final String INTERNAL = " [Internal] ";
    public static final String INFO = " [Info] ";
    public static void log(String type, String msg){
        mediumTimestamp =  "[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "]";
        String timeStamp = mediumTimestamp;
        System.out.println(timeStamp +" "+type+" "+msg);
    }
    public static void logErr(String lvl, String msg){
        mediumTimestamp =  "[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "]";
        String timeStamp = mediumTimestamp;
        System.err.println(timeStamp +" "+lvl+" "+msg);
    }
}
