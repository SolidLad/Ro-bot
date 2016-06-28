package commands;

import commands.utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Created by jackbachman on 6/27/16.
 * commands.utils.Command that will output as the bot what you input as the other arguments.
 * Let me Commit
 */
public class Say implements Command {

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        event.getTextChannel().sendMessage(compileString(args));
        event.getMessage().deleteMessage();
    }

    private String compileString(String[] args) {
        if(args.length >= 2) {
            String finalString = "";
            for (int i = 1; i < args.length; i++) {
                finalString += args[i] + " ";
            }
            return finalString;
        }
        return "";
    }

}
