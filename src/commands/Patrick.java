package commands;

import commands.utils.Command;
import net.dv8tion.jda.audio.player.FilePlayer;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
/**
 * Created by jackbachman on 6/27/16.
 * Let me Commit
 */
public class Patrick implements Command {

    private FilePlayer player;

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        String suffix = args[1];
        File audioFile;
        HashMap<String, File> hashMap = new HashMap<>();
        String[] sounds = {
                "burp", "fuck you", "duwang", "correct", "fuk", "yare yare", "go on", "hello", "hi", "hold on"
                , "idk", "fart", "ha", "maybe", "ninja", "no", "oh god", "okay", "shit", "stop", "son of a bitch", "sup", "thanks", "wait one minute",
                "what!", "yes", "kono dio da", "dio", "its okay", "oh my", "thank you", "believe it"
        };
        for (int i = 0; i < 32; i++) {
            hashMap.put(sounds[i], new File("res\\audio\\" + i + ".wav"));
        }
        Collection<?> keys = hashMap.keySet();
        audioFile = null;
        System.out.println(suffix);
        for (Object key : keys) {
            if (suffix.equals(key)) {
                audioFile = hashMap.get(key);
                try {
                    player = new FilePlayer(audioFile);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
