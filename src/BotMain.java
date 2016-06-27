
import net.dv8tion.jda.*;

import javax.security.auth.login.LoginException;

public class BotMain {
    public static void main(String[] args)
    {
        try
        {
            new JDABuilder()
                    .setBotToken("MTk2MDMyNjY4NzcxNTQ5MTg0.Ck9JJg.7LPEGJ6vZGaBJjfR-byCCOhc7sQ")
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
