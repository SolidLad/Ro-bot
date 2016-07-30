package commands.text;

import utils.Command;
import utils.CommandHandler;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Set;

public class Help implements Command {

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        Set keys = CommandHandler.commands.keySet();
        String commands = "Commands: ```";
        for(Object key: keys) {
            commands += key.toString() + "\n";
        }
        commands += "```";
        event.getAuthor().getPrivateChannel().sendMessage(commands+"\nThank you for using Ro-bot!\nUse >>info to get more information about Ro-bot.\n" +
                "If you enjoy Ro-bot, please consider donating here <link goes here>.\nRo-bot is open source. The source code can be found here https://github.com/SolidLad/Ro-bot.\n" +
                "To add Ro-Bot to your server, click here: https://discordapp.com/oauth2/authorize?client_id=195963828842921986&scope=bot&permissions=0\n ");
    }
}
