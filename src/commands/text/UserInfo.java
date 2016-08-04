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
        new Thread(() ->{

            if (args.length>2){
                event.getTextChannel().sendMessage("Invalid Arguments");
                return;
            }
            String str = event.getMessage().getRawContent();
            User target = event.getJDA().getUserById(str.substring(14,str.length()-1) );
            String msg = "";
            msg += "Username: \""+target.getUsername()+"\"\n";
            msg += "ID: \""+target.getId()+"\"\n";
            if (target.getCurrentGame()!=null)
                msg += "Current Game: \""+target.getCurrentGame().getName()+"\"\n";
            msg += "Status: \""+target.getOnlineStatus().toString()+"\"\n";
            msg += "Avatar ID: \""+target.getAvatarId()+"\"\n";
            event.getTextChannel().sendMessage(new MessageBuilder().appendCodeBlock(msg,"js").build());

        }).run();

    }
    @Override
    public String level() {
        return "Everyone";
    }

    public String getDescription()
    {
        return "Returns info about a user  USAGE: **userinfo <username>";
    }

}
