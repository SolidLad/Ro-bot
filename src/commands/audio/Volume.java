package commands.audio;

import net.dv8tion.jda.player.MusicPlayer;
import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Volume implements Command{
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        try {
            if (event.getGuild().getAudioManager().getSendingHandler()!=null) {
                MusicPlayer player = (MusicPlayer) event.getGuild().getAudioManager().getSendingHandler();
                if (Float.valueOf(args[1])>1.0)
                    args[1] = "1.0";
                else if (Float.valueOf(args[1])<0.0)
                    args[1] = "0.0";
                player.setVolume(Float.valueOf(args[1]));
                event.getTextChannel().sendMessage("Set the volume to "+Float.valueOf(args[1]));
            }
            else event.getTextChannel().sendMessage("You must be playing a song to change its volume.");

        }
        catch (NumberFormatException e){
            e.printStackTrace();
        }

    }
}
