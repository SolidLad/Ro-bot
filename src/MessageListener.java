
import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.audio.player.FilePlayer;
import net.dv8tion.jda.audio.player.URLPlayer;
import net.dv8tion.jda.entities.*;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.user.UserOnlineStatusUpdateEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.player.Bot;
import net.dv8tion.jda.player.MusicPlayer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class MessageListener extends ListenerAdapter{
    private FilePlayer player;
    private URLPlayer urlPlayer;
    private final Timer soundTimer = new Timer();
    private final String commandCodeBlock =
            "```!roll                         | rolls a six sided die.\n" +
            "!users                           | shows a list of users and their online status.\n" +
            "!help                            | shows a list of commands\n" +
            "!patrick <sound>                 | plays a sound that patrick once made. '!patrick ?' will print the list of sounds."+
            "!test                            | for debugging. does nothing useful as of now." +
            "!D R E A M                       | links a youtube video" +
            "!say <message>                   | sends a message in the general channel." +
            "!play <url> <channel number>     | plays a song (currently only supports soundcloud)```";
    public MessageListener(){

    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        /**
         * ------------------------------------------------------------------
         *                             COMMANDS
         * ------------------------------------------------------------------
         */
        if (event.getMessage().equals("!stop")){
            player.play();
        }
        if (event.getMessage().getContent().startsWith("!say")) {
            event.getTextChannel().sendMessage(event.getMessage().getContent().substring(5));
            event.getMessage().deleteMessage();
        }
        if (!event.isPrivate())
            System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "] "+"[Logging]" + event.getMessage().getContent());
        if (event.getMessage().getContent().equals("!roll")) {
            Random random = new Random();
            event.getTextChannel().sendMessage("Rolled a " + (random.nextInt(5) + 1));
            return;
        }
        if (event.getMessage().getContent().equals("!D R E A M"))
            event.getTextChannel().sendMessage("Ｉ ｄｉｄ ｎＡＵＧＨＴｔ！ Ｍｉｓｔｅｒ Ｅｌｅｃｔｉｃ ｓｅｎｄ ｈｉｍ ｔｏ ｔｈｅ ｐｒｉｎｃｉｐａｌ＇ｓ ｏｆｆｉｃｅ ａｎｄ ｈａｖｅ ｈｉｍ ＥＸｐｅｌＬｅｄ！\uFEFF https://www.youtube.com/watch?v=CAtDt_qjQ4o");
        if (event.getMessage().getContent().equals("!users")) {
            event.getTextChannel().sendMessage("USERS: ");
            String msg = "";
            for (User aUser : event.getGuild().getUsers()
                    ) {
                msg = msg + "Username: " + aUser.getUsername() + " | Online Status: " + aUser.getOnlineStatus().toString() + "\n";
            }
            event.getChannel().sendMessage("\n[" + msg + "]");
            return;
        }
        if (event.getMessage().getContent().equals("!help")) {
            String prefix;
            prefix = event.getAuthor().getAsMention();
            event.getTextChannel().sendMessage(prefix + " A list of commands and their functions has been sent to your private messages.");
            event.getAuthor().getPrivateChannel().sendMessage(commandCodeBlock);
        }
        if (event.getMessage().getContent().equals("!test")) {
            event.getTextChannel().sendMessage("```This message is a test.```");
        }
        if (event.getMessage().getContent().startsWith("!play")) {
            new CustomPlayer(event, urlPlayer, soundTimer).playSound();
        }
        if (event.getMessage().getContent().startsWith("!patrick ")) {
            String suffix = event.getMessage().getContent().substring(9);
            if (suffix.equals("")) {
                event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + "invalid command");
            }

            File audioFile;
            HashMap<String , File> hashMap = new HashMap<>();
            String[] sounds = {
                    "burp","fuck you" , "duwang" , "correct" , "fuk" , "yare yare" , "go on" , "hello" , "hi" , "hold on"
                    , "idk" , "fart" , "ha" , "maybe" , "ninja" , "no" , "oh god" , "okay" , "shit" , "stop" , "son of a bitch" , "sup" , "thanks" , "wait one minute" ,
                    "what!" , "yes" , "kono dio da" , "dio" , "its okay" , "oh my" , "thank you" , "believe it"
            };
            if (suffix.equals("?")){
                String temp = "```Printing list of sounds:\n";
                for (int i = 0; i < sounds.length; i++) {
                    temp = temp + sounds[i] +"\n";
                }
                temp = temp + "```";
                event.getAuthor().getPrivateChannel().sendMessage(temp);
            }
            for (int i = 0; i < 32; i++) {
                hashMap.put(sounds[i], new File("res\\audio\\"+i+".wav"));
            }
            Collection<?> keys = hashMap.keySet();
            audioFile = null;
            System.out.println(suffix);
            for(Object key: keys){
                if (suffix.equals(key)) {
                    audioFile = hashMap.get(key);
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (audioFile!=null) {
                event.getGuild().getAudioManager().setSendingHandler(player);
                event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannels().get(0));
                player.play();
                soundTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        event.getGuild().getAudioManager().closeAudioConnection();
                    }
                }, (audioFile.length() / 44100) * 1000);
            }

        }
    }
    @Override
    public void onUserOnlineStatusUpdate(UserOnlineStatusUpdateEvent event) {
        /**
         * ------------------------------------------------------------
         *                       WELCOME MESSAGE
         * ------------------------------------------------------------
         */
        super.onUserOnlineStatusUpdate(event);
        if (event.getPreviousOnlineStatus() == OnlineStatus.OFFLINE) {
            User user = event.getUser();
            user.getPrivateChannel().sendMessage("Welcome back "+ user.getUsername()+"!");
        }
    }
}
