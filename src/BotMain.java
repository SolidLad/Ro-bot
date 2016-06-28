
import net.dv8tion.jda.*;
import net.dv8tion.jda.player.Bot;

import javax.security.auth.login.LoginException;

public class BotMain {
    public static void main(String[] args)
    {
        try
        {
            new JDABuilder()
                    .setBotToken("")
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
