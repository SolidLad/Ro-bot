package commands;
import commands.utils.Command;
import commands.utils.FileIO;
import net.dv8tion.jda.audio.player.URLPlayer;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Timer;
import java.util.TimerTask;
public class Play implements Command {

    private Timer soundTimer = new Timer();

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        String suffix;
        float volume;
        if (args.length>3) {
            volume = Float.valueOf(args[3]);
        }
        else volume = 1f;
        suffix = args[1];
        URL audioUrl = null;
        URLPlayer urlPlayer = null;
        //default value in case duration cant be retrieved.
        int length = 300000;
        int channel;
        if (args.length<3)
            //if channel not specified, play in general
            channel = 1;
        else channel = Integer.parseInt(args[2]);
        try {
            URL trackUrl = new URL(suffix);
            URLConnection con1 = trackUrl.openConnection();
            //open a url to get the location.
            URLConnection temp = new URL("http://api.soundcloud.com/resolve.json?url=" + suffix + "&client_id=" + FileIO.readStuff("stuff2.gitignore")).openConnection();
            HttpURLConnection tempCon = (HttpURLConnection) temp;
            //get the redirect url.
            String urlString = tempCon.getHeaderField("location");
            System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "][Internal] Response code:" + tempCon.getResponseCode());
            System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "][Internal] Playing SC track: " + suffix + " on channel: " + event.getGuild().getVoiceChannels().get(channel - 1)+" At volume:"+volume);
            audioUrl = new URL("http://api.soundcloud.com/tracks/" + urlString.substring(34, 43) + "/stream?client_id=" + FileIO.readStuff("stuff2.gitignore"));
            //manually redirect
            HttpURLConnection.setFollowRedirects(false);
            URLConnection con2 = audioUrl.openConnection();
            //get data from final destination.
            InputStream in = new URL("http://api.soundcloud.com/tracks/" + urlString.substring(34, 43) + "?client_id=" + FileIO.readStuff("stuff2.gitignore")).openStream();
            int i;
            char c;
            String data = "";
            try {
                while ((i = in.read()) != -1) {
                    c = (char) i;
                    data = data + String.valueOf(c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null)
                    in.close();
            }
            //get the length in milliseconds for later.
            length = Integer.parseInt(data.substring(data.indexOf("duration\":") + 10, data.indexOf(",\"commentable\"")));
            //actually get the location of the audio stream.
            audioUrl = new URL(con2.getHeaderField("Location"));
            //create the player
            urlPlayer = new URLPlayer(event.getJDA(), audioUrl, 4718592);
            //set the volume;
            urlPlayer.setVolume(volume);
            //disconnect.
            ((HttpURLConnection) con1).disconnect();
            ((HttpURLConnection) temp).disconnect();
            ((HttpURLConnection) con2).disconnect();

        } catch (UnsupportedAudioFileException | IOException e) {
            event.getTextChannel().sendMessage("Error: printing stacktrace: ```"+e.getMessage()+"```");
            e.printStackTrace();
        }
        if (audioUrl != null) {
            event.getGuild().getAudioManager().setSendingHandler(urlPlayer);
            event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannels().get(channel - 1));
            if (urlPlayer!=null&&!urlPlayer.isStopped())
                urlPlayer.play();
            else if (urlPlayer!=null)
                urlPlayer.restart();
            else {
                System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "][Error] Invalid URLPlayer. Was urlPlayer initialized?");
                event.getTextChannel().sendMessage("Error: Unable to resolve player.");
            }
            soundTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            }, length + 1000);
        }
    }
    //change
}
