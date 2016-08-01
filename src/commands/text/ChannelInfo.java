package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.Command;

import java.util.List;
import java.util.stream.Collectors;

public class ChannelInfo implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        if (args.length!=2)
            throw new MalformedCommandException();
        Guild guild = event.getGuild();
        List<TextChannel> tchannels = guild.getTextChannels();
        List<VoiceChannel> vchannels = guild.getVoiceChannels();
        List<TextChannel> ttargets = tchannels.parallelStream().filter(t -> t.getName().equalsIgnoreCase(args[1])).collect(Collectors.toList());
        List<VoiceChannel> vtargets = vchannels.parallelStream().filter(v -> v.getName().equalsIgnoreCase(args[1])).collect(Collectors.toList());
        String msg = "";
        if (ttargets.size()!=0){
            TextChannel target = ttargets.get(0);
            msg += "Name: \""+target.getName()+"\"\n";
            msg += "Server name: \""+target.getGuild().getName()+"\"\n";
            msg += "Total Messages: "+target.getHistory().retrieveAll().size()+"\n";
            msg += "ID: "+target.getId()+"\n";
            if (!target.getTopic().equals(""))
                msg += "Topic: \""+target.getTopic()+"\"\n";
            event.getTextChannel().sendMessage(new MessageBuilder().appendCodeBlock(msg,"js").build());
        }
        else if (vtargets.size()!=0){
            VoiceChannel target = vtargets.get(0);
            msg += "Name: \""+target.getName()+"\"\n";
            msg += "Server name: \""+target.getGuild().getName()+"\"\n";
            msg += "Bitrate: "+target.getBitrate()+"\n";
            if (target.getUserLimit()!=0)
                msg += "User limit: "+target.getUserLimit()+"\n";
            else msg += "User limit: \"none\"";

            msg += "ID: "+target.getId()+"\n";
            if (!target.getTopic().equals(""))
                msg += "Topic: \""+target.getTopic()+"\"\n";
            event.getTextChannel().sendMessage(new MessageBuilder().appendCodeBlock(msg,"js").build());
        }
        else throw new MalformedCommandException();

    }

    public String getDescription()
    {
        return "Returns info about the channel  USAGE: **channelinfo <channelname>";
    }
}
