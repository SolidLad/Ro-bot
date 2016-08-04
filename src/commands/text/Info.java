package commands.text;

import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Info implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        new Thread(() ->{

            event.getTextChannel().sendMessage("Command is empty, try again later please!");

        }).run();

    }
    @Override
    public String level() {
        return "Everyone";
    }
    public String getDescription()
    {
        return "Work is underway for this command";
    }
}
