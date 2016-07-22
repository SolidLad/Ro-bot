package commands.admin;

import commands.utils.BotLogger;
import commands.utils.Command;
import commands.utils.FileIO;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Shutdown implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        if (event.getMessage().isPrivate()&&event.getAuthor().getId().equals(FileIO.readFile("stuff4.gitignore"))) {
            String key = args[1];
            if (key.equals(FileIO.readFile("stuff3.gitignore")))
                event.getJDA().shutdown();
        }
        BotLogger.logErr(BotLogger.DANGER, "User: " + event.getAuthor() + "From guild: " + event.getGuild() + " Attempted to shut down the system with invalid access keycode.");

    }
}
