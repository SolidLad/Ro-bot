package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import utils.Command;
import utils.GuildManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Config implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        new Thread(() ->{
            if (args[1].equals("tags"))
                return;
            int requiredArgs = 3;
            if (event.isPrivate())
                requiredArgs = 4;
            if (args.length!=requiredArgs)
            {
                event.getTextChannel().sendMessage("Invalid Arguments");
                return;
            }
            JSONObject obj;
            if (!event.isPrivate()) {
                obj = new JSONObject(GuildManager.getConfig(event.getGuild()));
                if (obj.keySet().contains(args[1])) {
                    try {
                        obj.put(args[1], args[2]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            else {
                obj = new JSONObject(GuildManager.getConfig(event.getJDA().getGuildById(args[1])));
                if (obj.keySet().contains(args[2])) {
                    try {
                        obj.put(args[2], args[3]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            try {
                FileWriter writer;
                if (!event.isPrivate()) {
                    File file = new File("guild\\config\\"+event.getGuild().getId());
                    writer = new FileWriter(file);
                }
                else {
                    File file = new File("guild\\config\\"+event.getJDA().getGuildById(args[1]).getId());
                    writer = new FileWriter(file);
                }
                writer.write(obj.toString());
                writer.close();
                if (!event.isPrivate())
                    event.getTextChannel().sendMessage("Property "+args[1]+" was changed to " +args[2]);
                else event.getPrivateChannel().sendMessage("Property "+args[1]+" was changed to " +args[2]);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).run();

    }
    @Override
    public String level() {
        return "Admin";
    }

    @Override
    public String getDescription() {
        return "edits the config file. USAGE: **Config <Key><Value>";
    }
}
