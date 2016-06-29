package commands.text;

import com.sun.corba.se.impl.activation.CommandHandler;
import com.sun.corba.se.impl.util.PackagePrefixChecker;
import commands.utils.Command;
import commands.utils.ServerPackage;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Set;

/**
 * Created by jackbachman on 6/27/16.
 * Let me Commit
 */
public class Help implements Command {

    private final String commandCodeBlock = "```!ban\n!deafen\n!dream\n!help\n!kick\n!mute\n!patrick\n!play\n!poll\n!roll\n!say\n!stop\n!unban\n!undeafen\n!unmute\n!usage\n" +
            "!users\n```";

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        Set keys = ServerPackage.commands.keySet();
        String commands = "";
        for(Object key: keys) {
            commands += key.toString() + "\n";
        }
        event.getTextChannel().sendMessage(commands);
    }
}
