package commands.blacklist;
import org.json.JSONObject;
import utils.Command;
import net.dv8tion.jda.audio.AudioReceiveHandler;
import net.dv8tion.jda.audio.CombinedAudio;
import net.dv8tion.jda.audio.UserAudio;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.tritonus.sampled.file.WaveAudioFileWriter;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class Record implements Command{
    private int iterations = 0;
    private byte[] data;
    private String[] args;
    private MessageReceivedEvent event;
    private AudioReceiveHandler handler = new AudioReceiveHandler() {
        @Override
        public boolean canReceiveCombined() {
            return true;
        }
        @Override
        public boolean canReceiveUser() {
            return false;
        }
        @Override
        public void handleCombinedAudio(CombinedAudio combinedAudio) {
            byte[] pcm = combinedAudio.getAudioData(1.0D);
            data = ArrayUtils.addAll(data, pcm);
            iterations++;
            if (iterations>15000)
                {
                    event.getGuild().getAudioManager().setReceivingHandler(null);
                    endRecording(event);
                }
        }

        @Override
        public void handleUserAudio(UserAudio userAudio) {
        }

        @Override
        public void handleUserTalking(User user, boolean b) {

        }
    };
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        new Thread(() -> {
            if (new JSONObject(utils.GuildManager.getConfig(event.getGuild())).get("recordEnabled").equals(false)) {
                event.getTextChannel().sendMessage("Recording is disabled. Please enable it with **config recordEnabled true if you would like to use recording features.");
                return;
            }
            this.event = event;
            this.args = args;
            if (!event.getGuild().getAudioManager().isConnected()) {
                event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel());
                event.getGuild().getAudioManager().setReceivingHandler(handler);
                data = new byte[]{};
            } else {
                endRecording(event);
                event.getGuild().getAudioManager().closeAudioConnection();
                event.getGuild().getAudioManager().setReceivingHandler(new AudioReceiveHandler() {
                    @Override
                    public boolean canReceiveCombined() {
                        return false;
                    }

                    @Override
                    public boolean canReceiveUser() {
                        return false;
                    }

                    @Override
                    public void handleCombinedAudio(CombinedAudio combinedAudio) {


                    }

                    @Override
                    public void handleUserAudio(UserAudio userAudio) {

                    }

                    @Override
                    public void handleUserTalking(User user, boolean b) {

                    }
                });
            }
            iterations = 0;
        }).run();
    }
    public void endRecording(MessageReceivedEvent event){
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, AudioReceiveHandler.OUTPUT_FORMAT, data.length);
            File file = new File("guild\\"+event.getGuild().getName()+event.getGuild().getId()+"fileID"+System.nanoTime()+".wav");
            file.getParentFile().mkdirs();
            new WaveAudioFileWriter().write(audioInputStream, AudioFileFormat.Type.WAVE, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public String level() {
        return "Everyone";
    }

    public String getDescription()
    {
        return "Records audio in a channel  USAGE: **record";
    }
}
