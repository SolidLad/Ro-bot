package commands.audio;
import eventlisteners.PlayerEventListener;
import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.managers.GuildManager;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.source.AudioSource;
import net.dv8tion.jda.player.source.RemoteSource;
import org.json.JSONObject;
import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.FileIO;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Play implements Command {
    private PlayerEventListener listener;
    private AudioSource source;
    private static final List<String> YOUTUBE_DL_LAUNCH_ARGS = Collections.unmodifiableList(Arrays.asList("C:\\Program Files\\Python27\\python.exe", "youtube-dl", "-q", "-f", "bestaudio/best", "--no-playlist", "-o", "-"));
    private static final List<String> FFMPEG_LAUNCH_ARGS = Collections.unmodifiableList(Arrays.asList("ffmpeg.exe", "-i", "-", "-f", "s16be", "-ac", "2", "-ar", "48000", "-map", "a", "-"));

    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        new Thread(() -> {

            if (new JSONObject(utils.GuildManager.getConfig(event.getGuild())).get("musicEnabled").equals(false)) {
                event.getTextChannel().sendMessage("Music is disabled. Please enable it with **config musicEnabled true if you would like to use music features.");
                return;
            }
            if (args.length < 2) {
                event.getTextChannel().sendMessage("Invalid Arguments");
                return;
            }
            if (!args[1].startsWith("https://www.youtube.com/watch?v=")) {
                if (args.length == 2 && (args[1].startsWith("http://") || args[1].startsWith("https://"))) {
                    try {
                        source = new RemoteSource(args[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        InputStream in = new URL("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=relevance&fields=items(id)&q=" + compileString(args).replace(" ", "+") + "&key=" + FileIO.readFile("yt.secret")).openStream();
                        int i;
                        char c;
                        String str = "";
                        while ((i = in.read()) != -1) {
                            c = (char) i;
                            str += c;
                        }
                        str = str.substring(str.indexOf("video") + 24, str.indexOf("video") + 35);
                        in.close();
                        str = "https://www.youtube.com/watch?v=" + str;
                        source = new RemoteSource(str, YOUTUBE_DL_LAUNCH_ARGS, FFMPEG_LAUNCH_ARGS);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (source == null)
                source = new RemoteSource(args[1], YOUTUBE_DL_LAUNCH_ARGS, FFMPEG_LAUNCH_ARGS);
            MusicPlayer player;
            AudioManager manager = event.getGuild().getAudioManager();

            if (manager.getSendingHandler() == null) {
                player = new MusicPlayer();
                manager.setSendingHandler(player);
            } else player = ((MusicPlayer) manager.getSendingHandler());

            if (listener == null) {
                listener = new PlayerEventListener(event.getGuild(), event.getTextChannel());
                player.addEventListener(listener);
            }

            if (source != null && source.getInfo().getDuration().getMinutes() < 16) {
                player.getAudioQueue().add(source);
                event.getTextChannel().sendMessage(source.getInfo().getTitle() + " was added to the queue. Length `" + source.getInfo().getDuration().getTimestamp() + "`");
            } else
                event.getTextChannel().sendMessage("Invalid source. Either the video was longer than 15 minutes or the URL was invalid.");


            if (!event.getGuild().getAudioManager().isConnected() && event.getGuild().getAudioManager().getQueuedAudioConnection() == null)
                event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel());
            if (player.isStopped() && !player.isPlaying())
                player.play();
        }).run();
    }

    private String compileString(String[] args)  {
        if(args.length >= 2) {
            String finalString = "";
            for (int i = 1; i < args.length; i++) {
                if (i!=args.length-1)
                    finalString += args[i] + " ";
                else finalString += args[i];
            }
            return finalString;
        }
        return null;
    }
    @Override
    public String level() {
        return "Everyone";
    }

    public String getDescription()
    {
        return "Attempts to play audio from this list of supported sites https://rg3.github.io/youtube-dl/supportedsites.html NOTE: some of those may not work. If a string is provided instead of a link, the bot will search and play the first result  USAGE: **play <link>";
    }
}
