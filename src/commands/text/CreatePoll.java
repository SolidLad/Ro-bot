package commands.text;
import exceptions.MalformedCommandException;
import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import java.util.*;

/*
 * TODO:
 * WHAT DOES THIS CLASS NEED?
 * - Check for the correct arguments
 * - Make sure the voting system is NOT case sensitive
 * - Check if the voting option exists, if not then don't throw an exception, tell the user.
 * - Make sure that the user can't enter less than 2 options or more than 10 options.
 */

public class CreatePoll implements Command {

    private Map<String, Integer> poll = new HashMap<>();
    private List<String> votedUsers = new ArrayList<>();
    private MessageReceivedEvent pollChannelEvent;
    private Timer pollTimer = new Timer();

    private class FinishPoll extends TimerTask {

        @Override
        public void run() {
            printVotes();
            poll.clear();
            votedUsers.clear();
        }
    }

    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
        }
        if (args.length > 11 || args.length < 3 || duplicates(args))
            throw new MalformedCommandException("Invalid number of, or duplicate commands!");
        pollChannelEvent = event;
        poll.clear();
        votedUsers.clear();
        //Save the
        int timeReq = getTimeArgument(args);
        int timeVal = timeReq;
        //Total length of the options for the poll
        int length = args.length - 1;
        //If there wasn't a time argument then start options at args 1.
        if(timeReq <= -1) {
            //The default time delay is three minutes if none was specified
            timeVal = 180000;
            timeReq = 1;
        }
        //Else there was a time option and we have to skip over it.
        else {
            length -= 1;
            timeReq = 2;
        }
        for (int i = timeReq; i < args.length; i++) {
            poll.put(args[i], 0);
        }
        String message = "Vote up with " + length + " options!\nBelow are the possible options:\n";
        for (String key : poll.keySet()) {
            message += key + "\n";
        }
        pollChannelEvent.getTextChannel().sendMessage(message);
        pollTimer.schedule(new FinishPoll(), timeVal);
    }

    /*
        This function gets the time requirement in milliseconds,
        if there is no time req it will return -1.
     */
    private int getTimeArgument(String args[]) {
        if(args.length>=2&&args[1].indexOf("-t")==0) {
            //Save it
            String time = args[1];
            //Chop off the flag that tells us it is a time
            time = time.substring(time.indexOf("-t") + 2, time.length());
            //Save the postfix
            String postfix = time.substring(time.length()-1);
            //Chop off the postfix
            time = time.substring(0, time.length()-1);
            int timeVal = 0;
            switch(postfix) {
                //Minute case
                case("m"):
                    timeVal = 60000 * Integer.valueOf(time);
                    break;
                //Second case
                case("s"):
                    timeVal = 1000 * Integer.valueOf(time);
                    break;
            }
            return timeVal;
        }
        return -1;
    }

    protected void recordVote(MessageReceivedEvent event, String args[]) {
        String id = event.getAuthor().getId();
        //Make sure they haven't already voted
        if(!votedUsers.contains(id)) {
            //Put in the place of the option, the value of its votes + 1
            poll.put(args[1], poll.get(args[1]) + 1);
            //add the id to voted users
            votedUsers.add(id);
        }
    }

    private void printVotes() {
        String message = "Results of Polling:\n";
        //Set the highestOption as the first one by default
        String highestVoteOption = poll.keySet().iterator().next();
        for(String key: poll.keySet()) {
            if(poll.get(key) > poll.get(highestVoteOption)) {
                highestVoteOption = key;
            }
            message += key + ": " + poll.get(key) + "\n";
        }
        message += "Winner is " + highestVoteOption + ", with " + poll.get(highestVoteOption) + " votes!";
        pollChannelEvent.getTextChannel().sendMessage(message);
    }

    protected void endVote() {
        pollTimer.cancel();
        new FinishPoll().run();
    }
    private boolean duplicates(final String[] array)
    {
        Set<String> lump = new HashSet<>();
        for (String str : array)
        {
            if (lump.contains(str)) return true;
            lump.add(str);
        }
        return false;
    }

    public String getDescription()
    {
        return "Creates a poll USAGE: >>createpoll <option> <option> <option>....";
    }
}
