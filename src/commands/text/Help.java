package commands.text;

import com.sun.corba.se.impl.activation.CommandHandler;
import com.sun.corba.se.impl.util.PackagePrefixChecker;
import com.sun.corba.se.spi.activation.Server;
import commands.utils.Command;
import commands.utils.ServerPackage;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Set;

public class Help implements Command {

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        Set keys = ServerPackage.commands.keySet();
        String commands = "Commands: ```";
        for(Object key: keys) {
            commands += key.toString() + "\n";
        }
        commands += "```";
        event.getAuthor().getPrivateChannel().sendMessage(commands);
    }
}
