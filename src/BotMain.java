
import net.dv8tion.jda.*;
import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class BotMain {
    public static String token;
    public static String client_id;
    public static void main(String[] args)
    {
        client_id = readStuff("stuff2.gitignore");
        token = readStuff("stuff.gitignore");
        try
        {
            JDA builder = new JDABuilder()
                    .setBotToken(token)
                    .addListener(new MessageListener())
                    .buildBlocking();
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("The config was not populated. Please enter a bot token.");
        }
        catch (LoginException e)
        {
            System.out.println("The provided bot token was incorrect. Please provide valid details.");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    public static String readStuff(String fileName){
        try {
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            StringBuffer stringBuffer = new StringBuffer();
            int numCharsRead;
            char[] charArray = new char[1024];
            while ((numCharsRead = fileReader.read(charArray)) > 0) {
                stringBuffer.append(charArray, 0, numCharsRead);
            }
            fileReader.close();
             return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
