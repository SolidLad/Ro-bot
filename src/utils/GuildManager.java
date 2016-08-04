package utils;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class GuildManager {
    public void log(MessageReceivedEvent event, Guild guild){
        String path = "guild\\logs\\"+guild.getId()+"\\"+event.getTextChannel().getId();
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
    public static List<Permission> getBotPerms(Guild g){
        List<Role> botRoles = g.getRolesForUser(g.getJDA().getSelfInfo());
        List<Permission> perms = new LinkedList<>();
        for (Role r :
                botRoles) {
            perms.addAll(r.getPermissions());
        }
        return perms;
    }

    public static String getConfig(Guild guild) {
        return FileIO.readFile("guild\\config\\"+guild.getId());
    }
}
