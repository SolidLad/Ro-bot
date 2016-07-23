package commands.utils;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class GuildManager {
    public void log(MessageReceivedEvent event, Guild guild){
        String path = "guild\\logs\\"+guild.getName()+guild.getId()+".txt";
        FileIO.writeLog(path, event.getAuthor().getUsername()+": "+event.getMessage().getContent());
    }
}
