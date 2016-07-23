package commands.text;

import commands.utils.Command;
import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Info implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        event.getTextChannel().sendMessage("Command is empty, try again later please!");
    }
}
