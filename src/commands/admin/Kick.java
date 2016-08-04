package commands.admin;

import net.dv8tion.jda.Permission;
import utils.Command;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.GuildManager;

import java.util.LinkedList;
import java.util.List;

public class Kick implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        new Thread(()-> {
            GuildManager gm = new GuildManager(event.getGuild());
            List<User> users = event.getGuild().getUsers();
            User target = null;
            for (User aUser :
                    users) {
                if (aUser.getUsername().equals(args[1]))
                    target = aUser;
            }
            List<Role> authorRoles = event.getGuild().getRolesForUser(event.getAuthor());
            List<Permission> authorPerms = new LinkedList<>();
            for (Role r :
                    authorRoles) {
                authorPerms.addAll(r.getPermissions());
            }
            if ((utils.GuildManager.getBotPerms(event.getGuild()).contains(Permission.KICK_MEMBERS)&&authorPerms.contains(Permission.KICK_MEMBERS))||utils.GuildManager.getBotPerms(event.getGuild()).contains(Permission.ADMINISTRATOR)&&authorPerms.contains(Permission.ADMINISTRATOR)) {
                gm.kick(target);
            }
        }).run();

    }
    @Override
    public String level() {
        return "Admin";
    }

    public String getDescription()
    {
        return "Kicks a user from the server  USAGE: **kick <username>";
    }
}
