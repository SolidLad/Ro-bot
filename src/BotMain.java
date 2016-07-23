import utils.FileIO;
import net.dv8tion.jda.*;
import javax.security.auth.login.LoginException;

public class BotMain {
    public static String token;
    public static void main(String[] args)
    {
        token = FileIO.readFile("bot.secret");
        try
        {
            new JDABuilder()
                    .setBotToken(token.replaceAll("\n", ""))
                    .addListener(new MessageListener())
                    .setBulkDeleteSplittingEnabled(false)
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
