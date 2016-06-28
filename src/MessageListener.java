
import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.audio.player.FilePlayer;
import net.dv8tion.jda.audio.player.URLPlayer;
import net.dv8tion.jda.entities.*;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.user.UserOnlineStatusUpdateEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alex on 6/24/2016.
 */
public class MessageListener extends ListenerAdapter{
    private final CommandHandler commandHandler = new CommandHandler();

    private String getMessage(MessageReceivedEvent event) {
        return event.getMessage().getContent();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //Gets all possible commands and finds the one that you typed, then runs its main method.
        String[] args = getMessage(event).split(" ");
        commandHandler.commands.get(args[0]).run(event, args);
    }

    @Override
    public void onUserOnlineStatusUpdate(UserOnlineStatusUpdateEvent event) {
        super.onUserOnlineStatusUpdate(event);
        if (event.getPreviousOnlineStatus() == OnlineStatus.OFFLINE) {
            User user = event.getUser();
            user.getPrivateChannel().sendMessage("Welcome back "+ user.getUsername()+"!");
        }
    }
}
