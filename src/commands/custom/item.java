package commands.custom;
import commands.utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class item implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        event.getTextChannel().sendMessage("Greetings. I am a generated syntactically correct command！");
    }
}
