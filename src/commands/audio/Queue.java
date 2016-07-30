package commands.audio;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;
import utils.Command;

public class Queue implements Command {
    @Override
    //// FIXME: 7/29/2016 doesnt work for soundcloud EDIT: or playlists...
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        if (args.length!=1)
            throw new MalformedCommandException();
        MusicPlayer player = (MusicPlayer)event.getGuild().getAudioManager().getSendingHandler();
        if (player == null || player.getAudioQueue().size() == 0 && !player.isPlaying()) {
            event.getTextChannel().sendMessage("Queue is empty and no song is playing. Cannot display a queue that doesn't exist!");
            return;
        }
        event.getTextChannel().sendMessage("Gathering information, this may take a moment.");
        String str = "Title"+player.getCurrentAudioSource().getInfo().getTitle()+"`"+player.getCurrentAudioSource().getInfo().getDuration().getTimestamp()+"`";
        for (AudioSource source :
                player.getAudioQueue()) {
            AudioInfo info = source.getInfo();
            if (info.getDuration()!=null&&info.getTitle()!=null&&info.getError()==null)
                str += "Title: "+info.getTitle()+" | Duration: `"+info.getDuration().getTimestamp()+"`\n";

        }
        Message msg = new MessageBuilder()
                .appendString(str)
                .build();
        event.getTextChannel().sendMessage(msg);
    }
}
