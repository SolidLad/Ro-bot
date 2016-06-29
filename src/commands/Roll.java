package commands;

import commands.utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Random;

public class Roll implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        Random random = new Random();
        if (args.length==1) {
            event.getTextChannel().sendMessage("Rolled a " + (random.nextInt(5) + 1)+" on a six sided die.");
        }
        else if (args.length==2){
            try {
                event.getTextChannel().sendMessage("Rolled a " + (random.nextInt(Integer.parseInt(args[1])-1) + 1)+" on a "+args[1]+" sided die.");
            }
            catch (NumberFormatException ex) {
                event.getTextChannel().sendMessage("Invalid argument. Try ```!usage roll``` for more information on the ```roll``` command.");
            }
        }
        else event.getTextChannel().sendMessage("Invalid number of arguments. Try ```!usage roll``` for more information on the ```!roll``` command.");

    }
}
