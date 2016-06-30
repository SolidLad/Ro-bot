package commands.utils;

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
    public static void log(String timeStamp, String type,String msg){
        System.out.println(timeStamp+" "+type+" "+msg);
    }
    public static void logErr(String timeStamp, String lvl, String msg){
        System.err.println(timeStamp+" "+lvl+" "+msg);
    }
}
