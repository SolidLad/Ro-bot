package commands;

import commands.utils.BotLogger;
import commands.utils.Command;
import net.dv8tion.jda.audio.player.FilePlayer;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
public class Patrick implements Command {
    private String[] sounds = {
            "burp", "fuck you", "duwang", "correct", "fuk", "yare yare", "go on", "hello", "hi", "hold on"
            , "idk", "fart", "ha", "maybe", "ninja", "no", "oh god", "okay", "shit", "stop", "son of a bitch", "sup", "thanks", "wait one minute",
            "what!", "yes", "kono dio da", "dio", "its okay", "oh my", "thank you", "believe it"
    };
    private HashMap<String, File> hashMap = new HashMap<>();
    private FilePlayer player;
    public Patrick(){
        for (int i = 0; i < 32; i++) {
            hashMap.put(sounds[i], new File("res\\audio\\" + i + ".wav"));
        }
    }
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        String suffix = args[1];
        File audioFile;


        Collection<?> keys = hashMap.keySet();
        audioFile = null;
        for (Object key : keys) {
            if (suffix.equals(key)) {
                audioFile = hashMap.get(key);
                try {
                    player = new FilePlayer(audioFile);
                    event.getGuild().getAudioManager().setSendingHandler(player);
                    if (!event.getGuild().getAudioManager().isConnected()) {
                        if (!event.getGuild().getAudioManager().isConnected())
                            event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannels().get(0));
                        if (player!=null&&!player.isStopped())
                            player.play();
                        else if (player!=null)
                            player.restart();
                        else {
                            BotLogger.logErr("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "]", "[Severe]", "Invalid URLPlayer.");
                            event.getTextChannel().sendMessage("Error: Unable to resolve player.");
                        }
                        Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                event.getGuild().getAudioManager().closeAudioConnection();
                            }
                        }, ((7500)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
