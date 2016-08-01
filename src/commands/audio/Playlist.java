package commands.audio;

import eventlisteners.PlayerEventListener;
import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;
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
        event.getTextChannel().sendMessage("Gathering information, this may take some time.");
        for (AudioSource source : sources) {
            try {
                AudioInfo info = source.getInfo();
                System.out.println(info.getError());
                List<AudioSource> queue = player.getAudioQueue();
                if (info.getError()==null){
                    if (info.getDuration().getMinutes() < 16) {
                        queue.add(source);
                }
                } else
                    event.getTextChannel().sendMessage("A source was skipped because it caused an error or was longer than 15 minutes.");
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        if (!event.getGuild().getAudioManager().isConnected())
            event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel());
        if (player.isStopped()&& !player.isPlaying())
            player.play();
    }

    public String getDescription()
    {
        return "Plays a playlist from youtube  USAGE: **playlist <link>";
    }
}
