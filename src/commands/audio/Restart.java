package commands.audio;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.MusicPlayer;
import utils.Command;

public class Restart implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        if (args.length!=1)
            throw new MalformedCommandException();
        MusicPlayer player =((MusicPlayer) event.getGuild().getAudioManager().getSendingHandler());
        if (player.isStopped()) {
            player.reload(true);
            event.getTextChannel().sendMessage("Current song was restarted.");
        }
        else throw new MalformedCommandException();

    }
}
