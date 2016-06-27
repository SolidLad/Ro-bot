
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alex on 6/24/2016.
 */
public class MessageListener extends ListenerAdapter{
    private FilePlayer player;
    private URLPlayer urlPlayer;
    private final Timer soundTimer = new Timer();
    private final String commandCodeBlock =
            "```!roll            | rolls a six sided die.\n" +
            "!users           | shows a list of users and their online status.\n" +
            "!help            | shows a list of commands\n" +
            "!patrick <sound> | plays a sound that patrick once made. '!patrick ?' will print the list of sounds."+
            "!test            | for debugging. does nothing useful as of now." +
            "!D R E A M       | links a youtube video" +
            "!say <message>   | sends a message in the general channel." +
            "!play <url> <channel number>     | plays a song (currently only supports soundcloud)```";
    public MessageListener(){



    }




    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().equals("!stop")){
            player.stop();
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
            String suffix;
            if (event.getMessage().getContent().length()>5)
                 suffix = event.getMessage().getContent().substring(6);
            else suffix = null;
            URL audioUrl = null;
            urlPlayer = null;
            int length = 1000*300;
            int channel = Integer.parseInt(suffix.substring(suffix.length()-1));
            suffix = suffix.substring(0,suffix.length()-2);
            try {
                URL trackUrl = new URL(suffix);
                URLConnection con1 = trackUrl.openConnection();
                HttpURLConnection connection1 =(HttpURLConnection) con1;
                URLConnection temp = new URL("http://api.soundcloud.com/resolve.json?url="+suffix+"&client_id=e366fb8542ba5a50154f3a7dceb21c7d").openConnection();
                HttpURLConnection tempCon = (HttpURLConnection) temp;
                String urlString = tempCon.getHeaderField("location");
                System.out.println("["+ LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "][Internal] Response code:"+tempCon.getResponseCode());
                System.out.println("["+ LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "][Internal] Playing SC track " +suffix+" on channel:"+event.getGuild().getVoiceChannels().get(channel-1));
                audioUrl = new URL("http://api.soundcloud.com/tracks/"+urlString.substring(34,43)+"/stream?client_id=e366fb8542ba5a50154f3a7dceb21c7d");
                HttpURLConnection.setFollowRedirects(false);
                URLConnection con2 = audioUrl.openConnection();
                InputStream in = new URL("http://api.soundcloud.com/tracks/"+urlString.substring(34,43)+"?client_id=e366fb8542ba5a50154f3a7dceb21c7d").openStream();
                int i;
                char c;
                String data = "";
                try{
                    while((i=in.read())!=-1) {
                        c=(char)i;
                        data = data + String.valueOf(c);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    System.out.println(data);
                    if(in!=null)
                        in.close();
                }
                length = Integer.parseInt(data.substring(data.indexOf("duration\":")+10, data.indexOf(",\"commentable\"")));
                HttpURLConnection connection2 =(HttpURLConnection) con2;
                audioUrl = new URL(con2.getHeaderField("Location"));
                urlPlayer = new URLPlayer(event.getJDA(),audioUrl,4718592);
                ((HttpURLConnection) con1).disconnect();
                ((HttpURLConnection) temp).disconnect();
                ((HttpURLConnection) con2).disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (audioUrl!=null) {
                event.getGuild().getAudioManager().setSendingHandler(urlPlayer);
                event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannels().get(channel-1));
                if (!urlPlayer.isStopped())
                    urlPlayer.play();
                else urlPlayer.restart();
                soundTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        event.getGuild().getAudioManager().closeAudioConnection();
                    }
                }, length + 1000);
            }
        }
        if (event.getMessage().getContent().startsWith("!patrick ")) {
            String suffix = event.getMessage().getContent().substring(8);
            if (suffix.equals("")) {
                event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + "invalid command");
            }
            File audioFile;
            switch (suffix) {
                case " burp":
                    audioFile = new File("res\\audio\\0.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " fuck you":
                    audioFile = new File("res\\audio\\4.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " duwang": audioFile = new File("res\\audio\\1.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " correct": audioFile = new File("res\\audio\\2.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " fuk": audioFile = new File("res\\audio\\3.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " yare yare": audioFile = new File("res\\audio\\5.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " go on": audioFile = new File("res\\audio\\6.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " hello": audioFile = new File("res\\audio\\7.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " hi": audioFile = new File("res\\audio\\8.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " hold on": audioFile = new File("res\\audio\\9.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " idk": audioFile = new File("res\\audio\\10.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " fart": audioFile = new File("res\\audio\\11.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " ha": audioFile = new File("res\\audio\\12.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " maybe": audioFile = new File("res\\audio\\13.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " ninja": audioFile = new File("res\\audio\\14.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " no": audioFile = new File("res\\audio\\15.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " oh god": audioFile = new File("res\\audio\\16.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " okay": audioFile = new File("res\\audio\\17.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " shit": audioFile = new File("res\\audio\\18.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " stop": audioFile = new File("res\\audio\\19.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " son of a bitch": audioFile = new File("res\\audio\\20.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " sup": audioFile = new File("res\\audio\\21.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " thanks": audioFile = new File("res\\audio\\22.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " wait one minute": audioFile = new File("res\\audio\\23.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " what!": audioFile = new File("res\\audio\\24.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " yes": audioFile = new File("res\\audio\\25.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " kono dio da": audioFile = new File("res\\audio\\26.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " dio": audioFile = new File("res\\audio\\27.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " its okay": audioFile = new File("res\\audio\\28.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " oh my": audioFile = new File("res\\audio\\29.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " thank you": audioFile = new File("res\\audio\\30.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " believe it": audioFile = new File("res\\audio\\31.wav");
                    try {
                        player = new FilePlayer(audioFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                    break;
                case " ?":
                    event.getTextChannel().sendMessage(event.getAuthor().getAsMention()+"A list of sounds has been sent to your private messages.");
                    event.getAuthor().getPrivateChannel().sendMessage("Printing list of sounds.\n" +
                        "```burp\n" +
                        "fuck you\n" +
                        "duwang\n" +
                        "correct\n" +
                        "fuk\n" +
                        "yare yare\n" +
                        "go on\n" +
                        "hello\n" +
                        "hi\n" +
                        "hold on\n" +
                        "idk\n" +
                        "fart\n" +
                        "ha\n" +
                        "maybe\n" +
                        "ninja\n" +
                        "no\n" +
                        "oh god\n" +
                        "okay\n" +
                        "shit\n" +
                        "stop\n" +
                        "son of a bitch\n" +
                        "sup\n" +
                        "thanks\n" +
                        "wait one minute\n" +
                        "what!\n" +
                        "yes\n" +
                        "kono dio da\n" +
                        "dio\n" +
                        "its okay\n" +
                        "oh my\n" +
                        "thank you\n" +
                        "believe it\n"+
                        "```");
                    audioFile = null;
                    break;

                default:
                    audioFile = null;
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
        super.onUserOnlineStatusUpdate(event);
        if (event.getPreviousOnlineStatus() == OnlineStatus.OFFLINE) {
            User user = event.getUser();
            user.getPrivateChannel().sendMessage("Welcome back "+ user.getUsername()+"!");
        }
    }
}
