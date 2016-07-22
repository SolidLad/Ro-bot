import commands.utils.BotLogger;
import commands.utils.Command;
import commands.utils.CommandHandler;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter{
    private final CommandHandler commandHandler = new CommandHandler();

    private String getMessage(MessageReceivedEvent event) {
        return event.getMessage().getContent();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //Log message
        if (event.getMessage().isPrivate()) {
            BotLogger.log(BotLogger.LOGGING,  "USER:" + event.getAuthor().getUsername() + " Privately said: [" + event.getMessage().getContent() + "]");
        } else {
            BotLogger.log(BotLogger.LOGGING, "USER:" + event.getAuthor().getUsername() + " Said: [" + event.getMessage().getContent() + "]");
        }
        //Gets all possible commands and finds the one that you typed, then runs its main method.
        String[] args = getMessage(event).split(" ");
        Command cmd = commandHandler.commands.get(args[0]);
//        if (cmd!=null){
//           Thread thread = new SandboxThread(event, args);
//            thread.run();
//        }
        if (cmd != null) {
            cmd.run(event, args);
        }
    }
}
