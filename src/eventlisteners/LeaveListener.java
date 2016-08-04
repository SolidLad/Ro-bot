package eventlisteners;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LeaveListener extends ListenerAdapter {
    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        super.onGuildLeave(event);
        Guild g = event.getGuild();
        String name = "guild\\config\\" + g.getId();
        File file = new File(name);
        try {
            Files.delete(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
