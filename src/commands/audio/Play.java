package commands.audio;

import commands.utils.AudioManager;
import commands.utils.Command;
import commands.utils.ServerPackage;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Play implements Command {

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        ServerPackage.audioManager.addSong(event, args);
    }
}
