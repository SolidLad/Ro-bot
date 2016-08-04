package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.Command;
import utils.CommandHandler;

public class EndPoll implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        new Thread(() ->{
            CreatePoll  poll = (CreatePoll) CommandHandler.commands.get("**createpoll");
            poll.endVote();
        }).run();

    }
    @Override
    public String level() {
        return "Admin";
    }
    public String getDescription()
    {
        return "Ends a poll  USAGE: **endpoll";
    }
}
