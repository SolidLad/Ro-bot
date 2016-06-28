package commands.utils;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Created by jackbachman on 6/27/16.
 * PleaSe GoD Let me Commit
 */
public interface Command {
    void run(MessageReceivedEvent event, String[] args);
}
