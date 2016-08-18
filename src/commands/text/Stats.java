package commands.text;

import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import eventlisteners.MessageListener;

import java.lang.management.ManagementFactory;

public class Stats implements Command{
    private long startTime;
    public Stats(){
        startTime = System.currentTimeMillis();
    }
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        new Thread(()->{
            String msg = "```js\nUptime: "+(((System.currentTimeMillis()-startTime)/1000)/60) +" minute(s).\n" +
                    "Guilds: "+event.getJDA().getGuilds().size()+"\n" +
                    "Users: "+event.getJDA().getUsers().size()+"\n" +
                    "Developer: "+event.getJDA().getUserById("190652042493165568").getUsername()+"#8917"+"\n" +
                    "Total requests: "+ event.getJDA().getResponseTotal()+"\n" +
                    "Threads: "+ ManagementFactory.getThreadMXBean().getThreadCount()+"```";
            event.getChannel().sendMessage(msg);
        }).run();

    }
    @Override
    public String level() {
        return "Everyone";
    }

    public String getDescription()
    {
        return "Returns stats about server  USAGE: **stats";
    }
}
