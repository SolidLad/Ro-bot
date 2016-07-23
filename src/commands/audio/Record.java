package commands.audio;
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
    private byte[] data;
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
        if (!event.getGuild().getAudioManager().isConnected()) {
            event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel());
            event.getGuild().getAudioManager().setReceivingHandler(handler);
            data = new byte[] {};
        }
        else {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, AudioReceiveHandler.OUTPUT_FORMAT, data.length);
                File file = new File("res\\test.wav");
                new WaveAudioFileWriter().write(audioInputStream, AudioFileFormat.Type.WAVE, file);
            } catch (IOException e) {
                e.printStackTrace();
            }

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
    }
}
