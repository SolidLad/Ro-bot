package commands;

import commands.utils.AudioManager;
import commands.utils.Command;
import commands.utils.EventArgs;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Play implements Command {

    private AudioManager audioManager = new AudioManager();

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        audioManager.addSong(event, args);
    }

}
