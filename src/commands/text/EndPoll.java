package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.Command;
import utils.ServerPackage;

public class EndPoll implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        CreatePoll  poll = (CreatePoll) ServerPackage.commands.get(">>createpoll");
        poll.endVote();
    }
}
