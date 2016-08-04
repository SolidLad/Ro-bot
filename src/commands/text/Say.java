package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.entities.Guild;
import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.stream.Collectors;

public class Say implements Command {

    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        new Thread(() ->{
            String msg = compileString(args);
            if (msg!=null) {
                if (!event.getMessage().isPrivate()) {
                    event.getTextChannel().sendMessage(msg);
                } else {
                    List<Guild> sharedGuilds = event.getJDA().getGuilds().parallelStream().filter(g -> g.getUsers().contains(event.getAuthor())).collect(Collectors.toList());
                    Guild target = event.getJDA().getGuildById(args[1]);
                    if (sharedGuilds.contains(target)) {
                        target.getTextChannels().get(0).sendMessage(msg);
                    }
                }
            }
        }).run();

    }

    private String compileString(String[] args) {
        if(args.length >= 2) {
            String finalString = "";
            for (int i = 1; i < args.length; i++) {
                finalString += args[i] + " ";
            }
            return finalString;
        }
        return null;
    }
    @Override
    public String level() {
        return "Admin";
    }

    public String getDescription()
    {
        return "Makes bot repeat what you say  USAGE: **say <guildid> <message>";
    }
}
