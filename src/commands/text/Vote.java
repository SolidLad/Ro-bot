package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.Command;
import utils.ServerPackage;

/**
 * Created by jack on 7/23/16.
 */
public class Vote implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        //Get the poll class and record the vote
        CreatePoll poll = (CreatePoll) ServerPackage.commands.get(">>createpoll");
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
        }
        poll.recordVote(event, args);
    }
}
