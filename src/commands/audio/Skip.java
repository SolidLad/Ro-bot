package commands.audio;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.MusicPlayer;
import utils.Command;

public class Skip implements Command{
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        MusicPlayer player =((MusicPlayer) event.getGuild().getAudioManager().getSendingHandler());
        if (args.length==2){
            try {
                if (Integer.parseInt(args[1])<1)
                    throw new MalformedCommandException();
                for (int i = 0; i < Integer.valueOf(args[1]); i++) {
                    player.skipToNext();
                }
                event.getTextChannel().sendMessage(event.getTextChannel().sendMessage("Successfully skipped "+Integer.valueOf(args[1])+" songs."));
            }
            catch (NumberFormatException e){
                event.getTextChannel().sendMessage("Invalid arguments. Try >>usage Skip for more information");
                e.printStackTrace();
            }

        }
        if (args.length!=1)
            throw new MalformedCommandException();
        if (player.isStopped())
            throw new MalformedCommandException();
        player.skipToNext();
    }

    public String getDescription()
    {
        return "Skips the current playing song  USAGE: **skip";
    }
}
