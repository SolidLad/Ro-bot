package commands.audio;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.player.MusicPlayer;
import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Stop implements Command {

    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        new Thread(() ->{
            if (event.getGuild().getAudioManager().getSendingHandler()!=null) {
                MusicPlayer player = (MusicPlayer) event.getGuild().getAudioManager().getSendingHandler();
                if (player.isPlaying()){
                    player.getAudioQueue().clear();
                    player.stop();
                    event.getGuild().getAudioManager().closeAudioConnection();
                    event.getTextChannel().sendMessage("Successfully cleared the queue, stopped playback, and closed the connection.");
                }
                else {
                    {
                        event.getTextChannel().sendMessage("Invalid Arguments");
                        return;
                    }
                }
            }
        }).run();

    }
    @Override
    public String level() {
        return "Admin";
    }

    public String getDescription()
    {
        return "Stops all music in the voice channel and clears any queues  USAGE: **stop";
    }
}
