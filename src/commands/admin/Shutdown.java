package commands.admin;

import commands.utils.BotLogger;
import commands.utils.Command;
import commands.utils.FileIO;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Shutdown implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        if (event.getMessage().isPrivate()) {
            String key = "";
            for (int i = 1; i < args.length; i++) {
                if (i != args.length - 1)
                    key += args[i] + " ";
                else key += args[i];
            }
            if (key.equals(FileIO.readFile("stuff3.gitignore")))
                event.getJDA().shutdown();
            BotLogger.logErr("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "]", "[DANGER]", "User: " + event.getAuthor() + "From guild: " + event.getGuild() + " Attempted to shut down the system with invalid access keycode.");
        }
    }
}
