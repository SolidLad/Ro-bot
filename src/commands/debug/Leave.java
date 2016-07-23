package commands.debug;

import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Leave implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        event.getGuild().getAudioManager().closeAudioConnection();
    }
}
