package eventlisteners;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        super.onReady(event);
        for (Guild g :
                event.getJDA().getGuilds()) {
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
