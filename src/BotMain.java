import commands.utils.FileIO;
import net.dv8tion.jda.*;
import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
public class BotMain {
    public static String token;
    public static void main(String[] args)
    {
        token = FileIO.readFile("stuff.gitignore");
        try
        {
            new JDABuilder()
                    .setBotToken(token.replaceAll("\n", ""))
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
}
