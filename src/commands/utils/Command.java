package commands.utils;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
public interface Command {
    void run(MessageReceivedEvent event, String[] args);
}
