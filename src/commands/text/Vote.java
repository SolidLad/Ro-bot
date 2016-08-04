package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.Command;
import utils.CommandHandler;

public class Vote implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        new Thread(() ->{

            //Get the poll class and record the vote
            // FIXME: 7/25/2016 causes a error if poll does not yet exist. also possible issues with multiple polls running on different guilds
            CreatePoll poll = (CreatePoll) CommandHandler.commands.get("**createpoll");
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i].toLowerCase();
            }
            poll.recordVote(event, args);
        }).run();
    }
    @Override
    public String level() {
        return "Everyone";
    }
    public String getDescription()
    {
        return "Vote in a poll  USAGE: **vote <option>";
    }
}
