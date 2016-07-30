package commands.text;

import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import eventlisteners.MessageListener;

public class Stats implements Command{
    private long startTime;
    public Stats(){
        startTime = System.currentTimeMillis();
    }
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        String msg = "```Uptime: "+(((System.currentTimeMillis()-startTime)/1000)/60) +" minute(s).\n" +
                "Guilds: "+event.getJDA().getGuilds().size()+"\n" +
                "Users: "+event.getJDA().getUsers().size()+"\n" +
                "Developer: "+event.getJDA().getUserById("190652042493165568").getUsername()+"\n" +
                "Total requests: "+ MessageListener.requests+"```";
        event.getChannel().sendMessage(msg);
    }
}
