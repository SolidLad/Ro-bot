package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.Command;

public class ServerInfo implements Command{
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        Guild guild = event.getGuild();
        String msg = "Name: \""+guild.getName()+"\"\n";
        msg += "ID: "+guild.getId()+"\n";
        msg += "Owner: \""+guild.getOwner().getUsername()+"\"\n";
        msg += "Roles: "+guild.getRoles().size()+"\n";
        msg += "Text Channels: "+guild.getTextChannels().size()+"\n";
        msg += "Voice Channels: "+guild.getVoiceChannels().size()+"\n";
        msg += "Users: "+guild.getUsers().size()+"";
        event.getTextChannel().sendMessage(new MessageBuilder().appendCodeBlock(msg, "js").build());
    }
}
