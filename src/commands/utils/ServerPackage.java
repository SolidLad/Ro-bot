package commands.utils;
import java.util.HashMap;
import java.util.Map;

public class ServerPackage {
    public final static AudioManager audioManager = new AudioManager();
    public static Map<String, Command> commands = new HashMap<>();
}
