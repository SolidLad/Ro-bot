package commands;

import commands.utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Created by jackbachman on 6/27/16.
 * Let me Commit
 */
public class Help implements Command {

    private final String commandCodeBlock = "````!ban\n!deafen\n!dream\n!help\n!kick\n!mute\n!patrick\n!play\n!roll\n!say\n!stop\n!unban\n!undeafen\n!unmute\n!usage\n" +
            "!users\n```";

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        String prefix;
        prefix = event.getAuthor().getAsMention();
        event.getTextChannel().sendMessage(prefix + " A list of commands and their functions has been sent to your private messages.");
        event.getAuthor().getPrivateChannel().sendMessage(commandCodeBlock);
    }
}
