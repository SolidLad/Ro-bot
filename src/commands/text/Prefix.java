package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import utils.Command;
import utils.GuildManager;

public class Prefix implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        new Thread(() ->{

            JSONObject obj = new JSONObject(GuildManager.getConfig(event.getGuild()));
            event.getTextChannel().sendMessage("My current prefix is: `"+ obj.get("prefix")+"`");

        }).run();

    }
    @Override
    public String level() {
        return "Everyone";
    }
    @Override
    public String getDescription() {
        return "Displays current prefix.";
    }
}
