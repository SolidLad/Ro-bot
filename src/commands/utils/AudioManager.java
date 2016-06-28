package commands.utils;

import net.dv8tion.jda.audio.player.URLPlayer;
import net.dv8tion.jda.entities.Message;
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
import java.util.*;
public class AudioManager {
    private ArrayList<Integer> durations = new ArrayList<>();
    private String sckey = FileIO.readStuff("stuff2.gitignore");
    private Timer soundTimer = new Timer();

    public synchronized void addSong(MessageReceivedEvent event, String[] args) {
        //If no song play the current song
        if (durations.size() == 0) {
            playNext(event, args);
            try {
                durations.add(calcLength(args));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            long totalTime = 0;
            for(int i = 0; i < durations.size(); i++) {
                totalTime += durations.get(i);
            }
            soundTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    playNext(event, args);
                    durations.remove(0);
                    if(durations.size() == 0) {
                        event.getGuild().getAudioManager().closeAudioConnection();
                    }
                }
            }, totalTime);
            try {
                durations.add(calcLength(args));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void playNext(MessageReceivedEvent event, String[] args){
        if (args[1]!=null) {
            String suffix = args[1];
            float volume;
            if (args.length>3) {
                volume = Float.valueOf(args[3]);
            }
            else volume = 1f;
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
                URLConnection temp = new URL("http://api.soundcloud.com/resolve.json?url=" + suffix + "&client_id=" + sckey).openConnection();
                HttpURLConnection tempCon = (HttpURLConnection) temp;
                //get the redirect url.
                String urlString = tempCon.getHeaderField("location");
                System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "][Internal] Response code:" + tempCon.getResponseCode());
                System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "][Internal] Playing SC track: " + suffix + " on channel: " + event.getGuild().getVoiceChannels().get(channel - 1)+" At volume:"+volume);
                audioUrl = new URL("http://api.soundcloud.com/tracks/" + urlString.substring(34, urlString.indexOf(".json")) + "/stream?client_id=" + sckey);
                //manually redirect
                HttpURLConnection.setFollowRedirects(false);
                URLConnection con2 = audioUrl.openConnection();
                //get data from final destination.
                InputStream in = new URL("http://api.soundcloud.com/tracks/" + urlString.substring(34, urlString.indexOf(".json")) + "?client_id=" + sckey).openStream();
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
                urlPlayer = new URLPlayer(event.getJDA(), audioUrl);
                //set the volume;
                urlPlayer.setVolume(volume);
                //disconnect.
                ((HttpURLConnection) con1).disconnect();
                ((HttpURLConnection) temp).disconnect();
                ((HttpURLConnection) con2).disconnect();

            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
            if (audioUrl != null) {
                event.getGuild().getAudioManager().setSendingHandler(urlPlayer);
                //if we're not connnected, connect.
                if (!event.getGuild().getAudioManager().isConnected())
                    event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannels().get(channel - 1));
                if (urlPlayer!=null&&!urlPlayer.isStopped())
                    urlPlayer.play();
                else if (urlPlayer!=null)
                    urlPlayer.restart();
                else {
                    System.err.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "][Error] Invalid URLPlayer.");
                    event.getTextChannel().sendMessage("Error: Unable to resolve player.");
                }
            }
        }
    }

    private int calcLength(String[] args) throws java.io.IOException{
        String suffix = args[1];
        int length;
        URLConnection temp = new URL("http://api.soundcloud.com/resolve.json?url=" + suffix + "&client_id=" + sckey).openConnection();
        HttpURLConnection tempCon = (HttpURLConnection) temp;
        String urlString = tempCon.getHeaderField("location");
        System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "][Internal] Response code:" + tempCon.getResponseCode());
        HttpURLConnection.setFollowRedirects(false);
        InputStream in = new URL("http://api.soundcloud.com/tracks/" + urlString.substring(34, urlString.indexOf(".json")) + "?client_id=" + sckey).openStream();
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
        length = Integer.parseInt(data.substring(data.indexOf("duration\":") + 10, data.indexOf(",\"commentable\"")));
        return length;
    }

    public void stop(MessageReceivedEvent event) {
        durations.clear();
        event.getGuild().getAudioManager().closeAudioConnection();
    }
    //asdf
}
