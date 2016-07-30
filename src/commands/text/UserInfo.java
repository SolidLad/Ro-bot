package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.Command;

import java.util.List;
import java.util.stream.Collectors;

public class UserInfo implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        //filter users into all the users that share a username with the target.
        List<User> targets = event.getGuild().getUsers().parallelStream().filter(user -> user.getUsername().equalsIgnoreCase(compileString(args))).collect(Collectors.toList());
        if (targets.size()==0)
            throw new MalformedCommandException();
        //if two targets share a name, get the first one.
        User target = targets.get(0);
        String msg = "";
        msg += "Username: \""+target.getUsername()+"\"\n";
        msg += "ID: \""+target.getId()+"\"\n";
        if (target.getCurrentGame()!=null)
            msg += "Current Game: \""+target.getCurrentGame().getName()+"\"\n";
        msg += "Status: \""+target.getOnlineStatus().toString()+"\"\n";
        msg += "Avatar ID: \""+target.getAvatarId()+"\"\n";
        event.getTextChannel().sendMessage(new MessageBuilder().appendCodeBlock(msg,"js").build());
    }
    private String compileString(String[] args)  {
        if(args.length >= 2) {
            String finalString = "";
            for (int i = 1; i < args.length; i++) {
                if (i!=args.length-1)
                    finalString += args[i] + " ";
                else finalString += args[i];
            }
            return finalString;
        }
        return null;
    }

}
