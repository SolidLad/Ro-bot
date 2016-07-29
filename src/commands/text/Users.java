package commands.text;

import utils.Command;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Users implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        String msg = "USERS: ";
        for (User aUser : event.getGuild().getUsers()
                ) {
            msg = msg + "Username: " + aUser.getUsername() + " | Online Status: " + aUser.getOnlineStatus().name() + "\n";
        }
        event.getChannel().sendMessage("\n```" + msg + "```");
    }
}
