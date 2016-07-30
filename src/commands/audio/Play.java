package commands.audio;

import eventlisteners.PlayerEventListener;
import exceptions.MalformedCommandException;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.source.AudioSource;
import net.dv8tion.jda.player.source.RemoteSource;
import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Play implements Command {
    private PlayerEventListener listener;

    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        if (args.length!=2)
            throw new MalformedCommandException();
        final List<String> YOUTUBE_DL_LAUNCH_ARGS = Collections.unmodifiableList(Arrays.asList("C:\\Program Files\\Python27\\python.exe", "youtube-dl", "-q", "-f", "bestaudio/best", "--no-playlist", "-o", "-"));
        final List<String> FFMPEG_LAUNCH_ARGS = Collections.unmodifiableList(Arrays.asList("ffmpeg.exe", "-i", "-", "-f", "s16be", "-ac", "2", "-ar", "48000", "-map", "a", "-"));
        AudioSource source = new RemoteSource(args[1], YOUTUBE_DL_LAUNCH_ARGS, FFMPEG_LAUNCH_ARGS);
        MusicPlayer player;
        AudioManager manager = event.getGuild().getAudioManager();

        if (manager.getSendingHandler()==null) {
            player = new MusicPlayer();
            manager.setSendingHandler(player);
        }

        else player = ((MusicPlayer) manager.getSendingHandler());

        if (listener==null) {
            listener = new PlayerEventListener(event.getGuild(), event.getTextChannel());
            player.addEventListener(listener);
        }

        if (source!=null&&source.getInfo().getDuration().getMinutes()<16){
            player.getAudioQueue().add(source);
            event.getTextChannel().sendMessage(source.getInfo().getTitle()+" was added to the queue. Length `"+source.getInfo().getDuration().getTimestamp()+"`");
        }
        else event.getTextChannel().sendMessage("Invalid source. Either the video was longer than 15 minutes or the URL was invalid.");


        if (!event.getGuild().getAudioManager().isConnected())
            event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel());
        if (player.isStopped()&& !player.isPlaying())
            player.play();
    }
}
