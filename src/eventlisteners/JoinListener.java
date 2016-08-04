package eventlisteners;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JoinListener extends ListenerAdapter {
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        super.onGuildJoin(event);
        Guild g = event.getGuild();
        String name = "guild\\config\\"+g.getId();
        File file = new File(name);
        if (!file.exists()){
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(createConfig(g).toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private JSONObject createConfig(Guild g){
        JSONObject obj = new JSONObject();
        obj.put("prefix","**");
        obj.put("musicEnabled",true);
        obj.put("recordEnabled",true);
        obj.put("greeting", "Welcome to "+g.getName()+"!");
        obj.accumulate("tags", "");
        return obj;
    }
}
