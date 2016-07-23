package commands.text;
import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.*;

public class CreatePoll implements Command {

    private Map<String, Integer> poll = new HashMap<>();
    private List<String> votedUsers = new ArrayList<>();
    private MessageReceivedEvent pollChannelEvent;
    private Timer pollTimer = new Timer();

    private class finishPoll extends TimerTask {

        @Override
        public void run() {
            poll.clear();
            votedUsers.clear();
            printVotes();
        }
    }

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        pollChannelEvent = event;
        poll.clear();
        votedUsers.clear();
        for(int i = 1; i < args.length; i++) {
            poll.put(args[i], 0);
        }
        event.getTextChannel().sendMessage("Vote up with " + (args.length - 1) + " options!\nBelow are the possible options:");
        for(String key: poll.keySet()) {
            event.getTextChannel().sendMessage(key);
        }
        pollTimer.schedule(new finishPoll(), 30000);
    }

    protected void recordVote(MessageReceivedEvent event, String args[]) {
        String id = event.getAuthor().getId();
        //Make sure they haven't already voted
        if(!votedUsers.contains(id)) {
            //Put in the place of the option, the value of its votes + 1
            poll.put(args[1], poll.get(args[1]) + 1);
        }
    }

    private void printVotes() {
        pollChannelEvent.getTextChannel().sendMessage("Results of Polling:");
        //Set the highestOption as the first one by default
        //TODO: This line is broken
        String highestVoteOption = poll.keySet().iterator().next();
        for(String key: poll.keySet()) {
            if(poll.get(key) > poll.get(highestVoteOption)) {
                highestVoteOption = key;
            }
            pollChannelEvent.getTextChannel().sendMessage(key + ": " + poll.get(key));
        }
        pollChannelEvent.getTextChannel().sendMessage("Winner is " + highestVoteOption + ", with " + poll.get(highestVoteOption) + " votes!");
    }
}
