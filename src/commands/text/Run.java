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
        //splits the message in the code block by \n.
        String[] commands = msg.split("\n");
        for (String aCommand :
                commands) {
            //then each command into its arguments.
            String[] arguments = aCommand.split(" ");
            Command command = CommandHandler.commands.get(arguments[0]);
            if (command!=null)
                //if the command exists, run it.
                command.run(event, arguments);
            else
                throw new MalformedCommandException();
        }
    }
}
