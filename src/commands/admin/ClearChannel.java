package commands.admin;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.Command;
import utils.GuildManager;

import java.util.Timer;
import java.util.TimerTask;

public class ClearChannel implements Command
{
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException
    {
        new Thread(() -> {
            if (args.length!=2|| !args[1].equalsIgnoreCase("iAmCertain")) {
                event.getTextChannel().sendMessage("Malformed command.");
            }
            else {
                MessageHistory hist = event.getTextChannel().getHistory();
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (!cleared(event))
                            event.getTextChannel().deleteMessages(hist.retrieve(100));
                        else timer.cancel();
                    }
                };
                if (GuildManager.getBotPerms(event.getGuild()).contains(Permission.MESSAGE_MANAGE)||GuildManager.getBotPerms(event.getGuild()).contains(Permission.ADMINISTRATOR))
                    timer.scheduleAtFixedRate(task, 1000, 2000);
            }
        }).run();
    }

    @Override
    public String level() {
        return "Admin";
    }

    private boolean cleared(MessageReceivedEvent event){
        if (event.getTextChannel().getHistory().retrieveAll().size()==0) {
            event.getTextChannel().sendMessage("Channel successfully cleared.");
            return true;
        }
        else return false;
    }

    public String getDescription()
    {
        return "Clears a chat channel  USAGE: **clearchannel iamcertain";
    }

}



