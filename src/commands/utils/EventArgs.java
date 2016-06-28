package commands.utils;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Created by jackbachman on 6/28/16.
 */
public class EventArgs {
    private String[] args;
    private MessageReceivedEvent event;

    public EventArgs(MessageReceivedEvent event, String[] args) {
        this.event = event;
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }
}
