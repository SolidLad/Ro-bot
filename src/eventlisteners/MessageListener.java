package eventlisteners;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import utils.BotLogger;
import utils.Command;
import utils.CommandHandler;
import utils.GuildManager;

public class MessageListener extends ListenerAdapter{
    private final CommandHandler commandHandler = new CommandHandler();
    private final GuildManager guildManager = new GuildManager();
    public static long requests = 0;

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
            guildManager.log(event,event.getGuild());
        }
        //make sure bot cant be called by another bot to prevent loops.
        if (event.getAuthor().isBot())
            return;
        //Gets all possible commands and finds the one that you typed, then runs its main method.
        String[] args = getMessage(event).split(" ");
        Command cmd = commandHandler.commands.get(args[0]);
//        if (cmd!=null){
//           Thread thread = new SandboxThread(event, args);
//            thread.run();
//        }
        if (cmd != null) {
            try {
                cmd.run(event, args);
                requests++;
            } catch (MalformedCommandException e) {
                BotLogger.log(BotLogger.ERROR, "Malformed " + args[0].substring(2) + " command");
            }
        }
    }
}
