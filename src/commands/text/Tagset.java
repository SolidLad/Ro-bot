package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Command;
import utils.GuildManager;
import java.util.Iterator;

public class Tagset implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        new Thread(()->{
            Guild g = event.getGuild();
            JSONObject obj = new JSONObject(GuildManager.getConfig(g));
            JSONArray arr = obj.getJSONArray("tags");
            Iterator<Object> it = arr.iterator();
            String str = "";
            while (it.hasNext()){
                str += it.next().toString().split(":",2)[0]+"\n";
            }
            if (str.length()<1950)
                event.getTextChannel().sendMessage("Current tags:\n```\n"+str+"```");
            else event.getTextChannel().sendMessage("Too many tags to list.");
        }).run();
    }

    @Override
    public String level() {
        return "Everyone";
    }

    @Override
    public String getDescription() {
        return "Shows a list of tags.";
    }
}
