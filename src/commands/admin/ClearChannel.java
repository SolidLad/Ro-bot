package commands.admin;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.Command;

import java.util.Timer;
import java.util.TimerTask;

public class ClearChannel implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        if (args.length!=2|| !args[1].equalsIgnoreCase("iAcceptTheConsequences"))
            throw new MalformedCommandException();
        else {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (!cleared(event))
                        event.getTextChannel().deleteMessages(event.getTextChannel().getHistory().retrieve(100));
                    else timer.cancel();
                }
            };
            timer.scheduleAtFixedRate(task,1000,2000);

        }
    }
    private boolean cleared(MessageReceivedEvent event){
        if (event.getTextChannel().getHistory().retrieveAll().size()==0) {
            event.getTextChannel().sendMessage("Channel successfully cleared.");
            return true;
        }
        else return false;
    }
}
