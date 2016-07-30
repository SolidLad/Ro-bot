package commands.audio;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.MusicPlayer;
import utils.Command;

public class Shuffle implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        if (event.getGuild().getAudioManager().getSendingHandler() instanceof MusicPlayer){
            MusicPlayer player = (MusicPlayer) event.getGuild().getAudioManager().getSendingHandler();
            try {
                if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false"))
                    throw new MalformedCommandException();
                player.setShuffle(Boolean.valueOf(args[1].toLowerCase()));
                event.getTextChannel().sendMessage("Shuffle was toggled to `"+args[1].toUpperCase()+"`");
            }
            catch (Exception e){
                e.printStackTrace();
                event.getTextChannel().sendMessage("Invalid arguments. Try >>usage shuffle");
            }

        }
    }
}
