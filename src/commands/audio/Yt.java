package commands.audio;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.audio.player.FilePlayer;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.Command;
import utils.FileIO;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class Yt implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        if (args.length!=2)
            throw new MalformedCommandException();
        Timer timer = new Timer();
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL("http://www.youtubeinmp3.com/fetch/?format=text&video=http://"+args[1]).openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            StringBuilder sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            output = sb.toString();
            output = output.substring(output.lastIndexOf("http"),output.length()-2);
            InputStream in = new URL(output).openStream();
            Path p = Paths.get("guild\\tempfiles\\tempfile"+System.nanoTime()+"id"+event.getGuild().getId()+".mp3");
            Files.copy(in, p);
            conn.disconnect();
            FilePlayer player = new FilePlayer();
            player.setAudioFile(p.toFile());
            if (!event.getGuild().getAudioManager().isConnected())
                event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel());
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            },FileIO.getDurationOfMp3(p.toFile())+1000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }
}
