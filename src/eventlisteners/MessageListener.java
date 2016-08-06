package eventlisteners;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import org.json.JSONObject;
import utils.BotLogger;
import utils.Command;
import utils.CommandHandler;
import utils.GuildManager;

import javax.management.relation.RoleStatus;
import java.util.List;

public class MessageListener extends ListenerAdapter{
    private final GuildManager guildManager = new GuildManager();

    private String getMessage(MessageReceivedEvent event) {
        return event.getMessage().getContent();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //Log message
        if (event.getMessage().isPrivate()) {
            BotLogger.log(BotLogger.LOGGING,  "USER:" + event.getAuthor().getUsername() + " Privately said: [" + event.getMessage().getContent() + "]");
        } else {
            BotLogger.log(BotLogger.LOGGING, "USER:" + event.getAuthor().getUsername() + " Said: [" + event.getMessage().getContent() + "]");
            guildManager.log(event,event.getGuild());
        }
        //make sure bot cant be called by another bot to prevent loops.
        if (event.getAuthor().isBot())
            return;
        //Gets all possible commands and finds the one that you typed, then runs its main method.
        String[] args = getMessage(event).split(" ");
        if (!event.getMessage().getContent().equals("**prefix")) {
            if (!event.getMessage().isPrivate())
                args[0] = args[0].replace(new JSONObject(GuildManager.getConfig(event.getGuild())).getString("prefix"), "**");
            else
                args[0] = args[0].replace(new JSONObject(GuildManager.getConfig(event.getJDA().getGuildById(args[1]))).getString("prefix"), "**");
        }
        Command cmd = CommandHandler.commands.get(args[0]);
        if (cmd != null) {
            try {
                if (cmd.level().equals("Everyone"))
                    cmd.run(event, args);
                if (cmd.level().equals("Admin")) {
                    List<Role> roles = event.getGuild().getRolesForUser(event.getAuthor());
                    for (Role r :
                            roles) {
                        if (r.getPermissions().contains(Permission.ADMINISTRATOR)) {
                            cmd.run(event, args);
                            return;
                        }
                    }
                    event.getTextChannel().sendMessage("You do not have the required permissions for this command.");
                }

            } catch (MalformedCommandException e) {
                BotLogger.log(BotLogger.ERROR, "Malformed " + args[0].substring(2) + " command");
            }
        }
    }
}
