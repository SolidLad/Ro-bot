
import commands.utils.Command;
import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.entities.*;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.user.UserOnlineStatusUpdateEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
public class MessageListener extends ListenerAdapter{
    private final CommandHandler commandHandler = new CommandHandler();

    private String getMessage(MessageReceivedEvent event) {
        return event.getMessage().getContent();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //Gets all possible commands and finds the one that you typed, then runs its main method.
        String[] args = getMessage(event).split(" ");
        Command cmd = commandHandler.commands.get(args[0]);
        if(cmd != null) {
            cmd.run(event, args);
        }
    }

    @Override
    public void onUserOnlineStatusUpdate(UserOnlineStatusUpdateEvent event) {
        super.onUserOnlineStatusUpdate(event);
        if (event.getPreviousOnlineStatus() == OnlineStatus.OFFLINE) {
            User user = event.getUser();
            user.getPrivateChannel().sendMessage("Welcome back "+ user.getUsername()+"!");
        }
    }
}
