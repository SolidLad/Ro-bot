package commands.audio;

import eventlisteners.PlayerEventListener;
import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;
import org.json.JSONObject;
import utils.Command;

import java.util.*;

public class Playlist implements Command {
    private PlayerEventListener listener;
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        new Thread(() ->{
            if (new JSONObject(utils.GuildManager.getConfig(event.getGuild())).get("musicEnabled").equals(false)){
                event.getTextChannel().sendMessage("Music is disabled. Please enable it with **config musicEnabled true if you would like to use music features.");
                return;
            }
            if (args.length!=2) {
                event.getTextChannel().sendMessage("Invalid Arguments");
                return;
            }
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
            int count = 0;
            for (AudioSource source : sources) {
                try {
                    AudioInfo info = source.getInfo();
                    List<AudioSource> queue = player.getAudioQueue();
                    if (info.getError()==null){
                        if (info.getDuration().getMinutes() < 16) {
                            queue.add(source);
                        }
                    } else count++;

                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            if (count>0)
                event.getTextChannel().sendMessage(count+" sources were skipped due to errors or having a length greater than 15 minutes");
            if (!event.getGuild().getAudioManager().isConnected())
                event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel());
            if (player.isStopped()&& !player.isPlaying())
                player.play();


        }).run();

    }
    @Override
    public String level() {
        return "Everyone";
    }

    public String getDescription()
    {
        return "Plays a playlist from youtube  USAGE: **playlist <link>";
    }
}
