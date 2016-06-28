package commands.utils;
public class BotLogger {
    public static void log(String timeStamp, String type,String msg){
        System.out.println(timeStamp+" "+type+" "+msg);
    }
    public static void logErr(String timeStamp, String lvl, String msg){
        System.err.println(timeStamp+" "+lvl+" "+msg);
    }
}
