package commands.debug;

import exceptions.MalformedCommandException;
import utils.Command;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Join implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException{
        if (args.length==1){
            VoiceChannel vc = event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel();
            event.getGuild().getAudioManager().openAudioConnection(vc);
        }
        else if (args.length==2)
            try {
                VoiceChannel vc = event.getGuild().getVoiceChannels().get(Integer.valueOf(args[1]));
                event.getGuild().getAudioManager().openAudioConnection(vc);
            }
            catch (NumberFormatException e){
                throw new MalformedCommandException();
            }
        else throw  new MalformedCommandException();
    }
    @Override
    public String level() {
        return "Admin";
    }
    public String getDescription()
    {
        return "Debug command";
    }
}
