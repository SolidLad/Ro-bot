package utils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GuildManager {
    public void log(MessageReceivedEvent event, Guild guild){
        String path = "guild\\logs\\"+guild.getName()+guild.getId()+"\\"+event.getTextChannel().getName()+".txt";
        String name =  event.getAuthor().getUsername()+": "+event.getMessage().getContent();
        try {
            Files.createDirectories(Paths.get(path.substring(path.indexOf(guild.getId())+guild.getId().length())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileIO.writeLog(path,name);
        try {
            if (Files.exists(Paths.get(path))&&FileIO.calcLines(path)>1000){
                if (!Files.exists(Paths.get(path).resolveSibling(event.getTextChannel().getName()+".old")))
                    Files.move(Paths.get(path), Paths.get(path).resolveSibling(event.getTextChannel().getName()+".old"));
                else {
                    Files.delete(Paths.get(path).resolveSibling(event.getTextChannel().getName()+".old"));
                    Files.move(Paths.get(path), Paths.get(path).resolveSibling(event.getTextChannel().getName()+".old"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
