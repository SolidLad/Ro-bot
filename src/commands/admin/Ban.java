package commands.admin;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.Permission;
import utils.Command;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.GuildManager;

import java.util.LinkedList;
import java.util.List;

public class Ban implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        new Thread(() -> {
            GuildManager gm = new GuildManager(event.getGuild());
            List<User> users = event.getGuild().getUsers();
            User target = null;
            String targetName = "";
            for (int i = 1; i < args.length-1; i++) {
                if (i!=args.length-2)
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
            List<Permission> authorPerms = new LinkedList<>();
            for (Role r :
                    authorRoles) {
                authorPerms.addAll(r.getPermissions());
            }
            if ((utils.GuildManager.getBotPerms(event.getGuild()).contains(Permission.BAN_MEMBERS)&&authorPerms.contains(Permission.BAN_MEMBERS))||utils.GuildManager.getBotPerms(event.getGuild()).contains(Permission.ADMINISTRATOR)&&authorPerms.contains(Permission.ADMINISTRATOR)) {
                gm.ban(target, Integer.parseInt(args[args.length-1]));
            }
        }).run();
    }

    @Override
    public String level() {
        return "Admin";
    }

    public String getDescription()
    {
        return "Bans a user  USAGE: **ban <username>";
    }

}
