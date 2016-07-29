package commands.audio;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.player.MusicPlayer;
import utils.Command;
import utils.ServerPackage;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Stop implements Command {

    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        if (event.getGuild().getAudioManager().getSendingHandler()!=null) {
            MusicPlayer player = (MusicPlayer) event.getGuild().getAudioManager().getSendingHandler();
            if (player.isPlaying()){
                player.getAudioQueue().clear();
                player.stop();
                event.getGuild().getAudioManager().closeAudioConnection();
            }
            else {
                throw new MalformedCommandException();
            }
        }
    }

}
