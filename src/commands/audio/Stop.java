package commands.audio;

import utils.Command;
import utils.ServerPackage;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Stop implements Command {

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        ServerPackage.audioManager.stop(event);
    }
}
