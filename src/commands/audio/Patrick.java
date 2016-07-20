package commands.audio;

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
            "burp", "fuck you", "duwang", "correct", "fuk", "good grief", "go on", "hello", "hi", "hold on"
            , "idk", "fart", "ha", "maybe", "ninja", "no", "oh god", "okay", "shit", "stop", "son of a bitch", "sup", "thanks", "wait one minute",
            "what", "yes", "kono dio da", "dio", "its okay", "oh my", "thank you", "believe it"
    };
    private HashMap<String, File> hashMap = new HashMap<>();
    private FilePlayer player;
    public Patrick(){
        for (int i = 0; i < 32; i++) {
            hashMap.put(sounds[i], new File("res\\audio\\" + (i) + ".wav"));
        }
    }
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        String suffix = "";
        for (int i = 1; i < args.length; i++) {
            if (!args[i].equals(args[args.length-1]))
                suffix += args[i] + " ";
            else suffix += args[i];

        }
        File audioFile = null;
        Collection<?> keys = hashMap.keySet();
        for (Object key : keys) {
            if (suffix.equals(key)) {
                try {
                    audioFile = hashMap.get(key);
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
                    AudioFormat format = audioInputStream.getFormat();
                    long frames = audioInputStream.getFrameLength();
                    double durationInSeconds = (frames+0.0) / format.getFrameRate();
                    player = new FilePlayer(audioFile);
                    event.getGuild().getAudioManager().setSendingHandler(player);
                    if (!event.getGuild().getAudioManager().isConnected()) {
                        if (!event.getGuild().getAudioManager().isConnected())
                            event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel());
                        if (player!=null&&!player.isStopped())
                            player.play();
                        else if (player!=null)
                            player.restart();
                        else {
                            BotLogger.logErr("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "]", BotLogger.ERROR, "Invalid URLPlayer.");
                            event.getTextChannel().sendMessage("Error: Unable to resolve player.");
                        }
                        Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                event.getGuild().getAudioManager().closeAudioConnection();
                            }
                        }, ((((int) durationInSeconds)*1000+5000)));
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
