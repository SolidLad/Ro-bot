package commands;

import commands.utils.Command;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.GuildManager;

import java.util.List;

public class Undeafen implements Command{
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        GuildManager gm = new GuildManager(event.getGuild());
        List<User> users = event.getGuild().getUsers();
        User target = null;
        for (User aUser :
                users) {
            if (aUser.getUsername().equals(args[1]))
                target = aUser;
        }
        List<Role> authorRoles = event.getGuild().getRolesForUser(event.getAuthor());
        if (target!=null){//&&authorRoles.contains(event.getGuild().getRoleById("Admin"))) {
            gm.undeafen(target);
        }
    }
}
