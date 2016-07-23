package commands.text;

import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Info implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        event.getTextChannel().sendMessage("Command is empty, try again later please!");
    }
}
