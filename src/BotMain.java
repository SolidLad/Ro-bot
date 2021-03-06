import eventlisteners.JoinListener;
import eventlisteners.ReadyListener;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.managers.AccountManager;
import utils.CommandHandler;
import utils.FileIO;
import eventlisteners.MessageListener;

import javax.security.auth.login.LoginException;

public class BotMain {
    public static String token;
    public static void main(String[] args)
    {
        token = FileIO.readFile("bot.secret");
        try
        {
            JDA jda = new JDABuilder()
                    .setBotToken(token.replaceAll("\n", ""))
                    .addListener(new ReadyListener())
                    .addListener(new JoinListener())
                    .addListener(new MessageListener())
                    .setBulkDeleteSplittingEnabled(false)
                    .buildBlocking();
            new CommandHandler();
            jda.getAccountManager().setGame("**help");
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
