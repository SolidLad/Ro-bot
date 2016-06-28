package commands;

import commands.utils.AudioManager;
import commands.utils.Command;
import commands.utils.ServerPackage;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Created by jackbachman on 6/28/16.
 */
public class Stop implements Command {

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        ServerPackage.audioManager.stop(event);
    }
}
