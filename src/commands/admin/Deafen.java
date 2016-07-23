package commands.admin;

import utils.Command;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.GuildManager;

import java.util.List;

public class Deafen implements Command{
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        GuildManager gm = new GuildManager(event.getGuild());
        List<User> users = event.getGuild().getUsers();
        User target = null;
        String targetName = "";
        for (int i = 1; i < args.length; i++) {
            if (i!=args.length-1)
                targetName += args[i]+" ";
            else targetName += args[i];

        }
        for (User aUser :
                users) {
            if (aUser.getUsername().equalsIgnoreCase(targetName)) {
                target = aUser;
            }
        }
        List<Role> authorRoles = event.getGuild().getRolesForUser(event.getAuthor());
        if (target!=null&&authorRoles.contains(event.getGuild().getRoleById("194965592774541322"))) {
            gm.deafen(target);
        }
    }
}
