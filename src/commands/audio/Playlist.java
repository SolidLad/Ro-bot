package commands.audio;

import eventlisteners.PlayerEventListener;
import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.source.AudioSource;
import net.dv8tion.jda.player.source.RemoteSource;
import utils.Command;

import java.util.*;

public class Playlist implements Command {
    private PlayerEventListener listener;
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        if (args.length!=2)
            throw new MalformedCommandException();
        net.dv8tion.jda.player.Playlist playlist = net.dv8tion.jda.player.Playlist.getPlaylist(args[1]);
        List<AudioSource> sources = new LinkedList<>(playlist.getSources());
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
        for (AudioSource source : sources) {
            if (source.getInfo().getDuration().getMinutes()<16) {
                List<AudioSource> queue = player.getAudioQueue();
                queue.add(source);
            }
            else event.getTextChannel().sendMessage("A source was skipped because it was longer than 15 minutes");
        }
        if (!event.getGuild().getAudioManager().isConnected())
            event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel());
        if (player.isStopped()&& !player.isPlaying())
            player.play();
    }

    public String getDescription()
    {
        return "Plays a playlist from youtube  USAGE: >>playlist <link>";
    }
}
