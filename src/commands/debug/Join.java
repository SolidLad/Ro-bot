package commands.debug;

import utils.Command;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Join implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        VoiceChannel vc = event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel();
        event.getGuild().getAudioManager().openAudioConnection(vc);
    }
}
