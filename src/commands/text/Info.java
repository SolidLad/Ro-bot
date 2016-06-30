package commands.text;

import commands.utils.Command;
import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Info implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        String targetName = "";
        for (int i = 1; i < args.length-1; i++) {
            if (i!=args.length-2)
                targetName += args[i]+" ";
            else targetName += args[i];

        }
        for (User user :
                event.getGuild().getUsers()) {
            if (targetName.equalsIgnoreCase(user.getUsername())){
                event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " ```" +
                        "Username: " + user.getUsername() +"\n"+
                        "Discriminator: " + user.getDiscriminator() +"\n"+
                        "Online Status: " + user.getOnlineStatus() +"\n"+
                        "Current game: " + user.getCurrentGame() +"\n"+
                        "ID: " + user.getId()+"```"
                );
                break;
            }
        }
        for (VoiceChannel channel :
                event.getGuild().getVoiceChannels()) {
            if (targetName.equalsIgnoreCase(channel.getName())){
                event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " ```" +
                        "Name :" + channel.getName() +"\n"+
                        "Guild :" + channel.getGuild() +"\n"+
                        "Id :" + channel.getId() +"\n"+
                        "Topic :" + channel.getTopic() +"\n"+
                        "Get User Limit :" + channel.getUserLimit() +"\n"+
                        "Bitrate :" + channel.getBitrate() + "```"
                );
            }
        }
        for (TextChannel channel :
                event.getGuild().getTextChannels()) {
            if (targetName.equalsIgnoreCase(channel.getName())){
                event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " ```" +
                        "Name :" + channel.getName() +"\n"+
                        "Guild :" + channel.getGuild() +"\n"+
                        "Id :" + channel.getId() +"\n"+
                        "Topic :" + channel.getTopic() + "```"
                );
            }
        }
    }
}
