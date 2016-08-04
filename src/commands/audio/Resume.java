package commands.audio;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.MusicPlayer;
import utils.Command;

public class Resume implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        new Thread(() ->{
            if (args.length!=1){
                event.getTextChannel().sendMessage("Invalid Arguments");
                return;
            }
            MusicPlayer player =((MusicPlayer) event.getGuild().getAudioManager().getSendingHandler());
            if (player.isStopped()){
                event.getTextChannel().sendMessage("Invalid Arguments");
                return;
            }
            player.play();
            event.getTextChannel().sendMessage("Playback was resumed");
        }).run();

    }
    @Override
    public String level() {
        return "Admin";
    }

    public String getDescription()
    {
        return "Resumes a paused song  USAGE: **resume";
    }
}
