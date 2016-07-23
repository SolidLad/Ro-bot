package commands.text;

import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
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
