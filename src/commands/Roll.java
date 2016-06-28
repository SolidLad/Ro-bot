package commands;

import commands.utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Random;

public class Roll implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        Random random = new Random();
        event.getTextChannel().sendMessage("Rolled a " + (random.nextInt(5) + 1));
    }
}
