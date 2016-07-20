package commands.audio;

import commands.utils.AudioManager;
import commands.utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Volume implements Command{
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        AudioManager.getUrlPlayer().setVolume(Float.valueOf(args[1]));
        event.getTextChannel().sendMessage("Set the volume to "+Float.valueOf(args[1]));
    }
}
