package commands.text;
import exceptions.MalformedCommandException;
import utils.Command;
import utils.CommandHandler;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Run implements Command{

    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        String msg = event.getMessage().getContent();
        msg = msg.substring(msg.indexOf("```")+3,msg.length()-3);
        String[] commands = msg.split("\n");
        for (String aCommand :
                commands) {
            String[] arguments = aCommand.split(" ");
            Command command = CommandHandler.commands.get(arguments[0]);
            if (command!=null)
                command.run(event, arguments);
            else
                throw new MalformedCommandException();
        }
    }

    public String getDescription()
    {
        return "Denied";
    }
}
