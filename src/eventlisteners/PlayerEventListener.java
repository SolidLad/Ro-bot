package eventlisteners;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.player.hooks.PlayerListenerAdapter;
import net.dv8tion.jda.player.hooks.events.FinishEvent;

public class PlayerEventListener extends PlayerListenerAdapter {
    private Guild guild;
    private TextChannel channel;
    public PlayerEventListener(Guild guild, TextChannel channel){
        this.guild = guild;
        this.channel = channel;
    }
    @Override
    public void onFinish(FinishEvent event) {
        super.onFinish(event);
        channel.sendMessage("Queue concluded. Shutting down...");
        guild.getAudioManager().setSendingHandler(null);
        guild.getAudioManager().closeAudioConnection();
    }
}
