package commands.text;
import exceptions.MalformedCommandException;
import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.CommandHandler;

import java.io.File;
import java.util.*;

public class Usage implements Command {


    private Map<String, String> commandsAndDescriptions = new HashMap<String, String>();
    public Usage()
    {
        //creates a map of commands and descriptions. ignores the prefix.
        Map<String, Command> map = CommandHandler.commands;
        for (String key :
                map.keySet()) {
            commandsAndDescriptions.put(key.substring(2), map.get(key).getDescription());
        }
    }
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException
    {
        new Thread(() ->{

            String message = "";
            //checking if the user provides the correct command syntax
            if(args.length > 1)
            {
                if (commandsAndDescriptions.keySet().contains(args[1]))
                    message = args[1] +"\t"+ commandsAndDescriptions.get(args[1]);
            }
            //checks for null message and sends if not null
            if (!message.equals(""))
            {
                event.getTextChannel().sendMessage(message);
            }
            //otherwise gives invalid usage command
            else
            {
                event.getTextChannel().sendMessage("Invalid usage command. Usage: `**usage <Command>`\nTry `**help` for a list of commands.");
            }

        }).run();

    }
    @Override
    public String level() {
        return "Everyone";
    }
    public String getDescription()
    {
        return "Returns the usage of a specific command.";
    }
}