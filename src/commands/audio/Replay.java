package commands.audio;
import commands.utils.Command;
import net.dv8tion.jda.audio.AudioReceiveHandler;
import net.dv8tion.jda.audio.CombinedAudio;
import net.dv8tion.jda.audio.UserAudio;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;

public class Replay implements Command{
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
            byte[] pcm_data = combinedAudio.getAudioData(1.0D);
            double L1 = 44100.0/240.0;
            double L2 = 44100.0/245.0;
            for (int i=0; i<pcm_data.length; i++) {
                pcm_data[i] = (byte)(55*Math.sin((i/L1)*Math.PI*2));
                pcm_data[i] += (byte)(55*Math.sin((i/L2)*Math.PI*2));
            }

            AudioFormat frmt = AudioReceiveHandler.OUTPUT_FORMAT;
            AudioInputStream ais = new AudioInputStream(
                    new ByteArrayInputStream(pcm_data), frmt,
                    pcm_data.length / frmt.getFrameSize()
            );

            try {
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new
                        File("res\\test.wav")
                );
                System.out.println("test");
            }
            catch(Exception e) {
                e.printStackTrace();
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
        event.getGuild().getAudioManager().setReceivingHandler(handler);
    }
}
