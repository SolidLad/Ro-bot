package commands.audio;

import utils.AudioManager;
import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Volume implements Command{
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        try {
            AudioManager.urlPlayer.setVolume(Float.valueOf(args[1]));
            event.getTextChannel().sendMessage("Set the volume to "+Float.valueOf(args[1]));
        }
        catch (NumberFormatException e){
            e.printStackTrace();
        }

    }
}
