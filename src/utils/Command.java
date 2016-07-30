package utils;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public interface Command {
    void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException;

    String getDescription();
}
