package utils;

import net.dv8tion.jda.audio.player.URLPlayer;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class AudioManager
{
    private String sckey = FileIO.readFile("sc.secret");
    private Timer soundTimer = new Timer();
    public static URLPlayer urlPlayer;
    private long endTimeOfQueue;
    MessageReceivedEvent lastEvent;
    private int queueSize = 0;

    private class KillQueue extends TimerTask {

        @Override
        public void run() {
            queueSize--;
            if(queueSize <= 0) {
                lastEvent.getGuild().getAudioManager().closeAudioConnection();
            }
        }
    }

    public synchronized void addSong(MessageReceivedEvent event, String[] args) {
        lastEvent = event;
        if(queueSize == 0) {
            endTimeOfQueue = System.currentTimeMillis();
        }
        try {
            if(args.length > 1) {
                queueSize++;
                int songLength = calcLength(args[1]);
                soundTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        playNext(event, args);
                    }
                }, new Date(endTimeOfQueue));
                //Set the queue so the next song starts at the end of this song
                endTimeOfQueue += songLength;
            } else {
                BotLogger.logErr(BotLogger.ERROR, "Malformed Command");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void playNext(MessageReceivedEvent event, String args[]){
        if (args.length > 1) {
            String suffix = args[1];
            URL audioUrl = null;
            try {
                URL trackUrl = new URL(suffix);
                URLConnection con1 = trackUrl.openConnection();
                //open a url to get the location.
                URLConnection temp = new URL("http://api.soundcloud.com/resolve.json?url=" + suffix + "&client_id=" + sckey).openConnection();
                HttpURLConnection tempCon = (HttpURLConnection) temp;
                //get the redirect url.
                String urlString = tempCon.getHeaderField("location");
                BotLogger.log("[Internal]"," Response code:" + tempCon.getResponseCode());
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
                //actually get the location of the audio stream.
                audioUrl = new URL(con2.getHeaderField("Location"));
                //create the player
                urlPlayer = new URLPlayer(event.getJDA(), audioUrl);
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
                    //connect to the authors currently connected channel
                    event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel());
                if (urlPlayer!=null&&!urlPlayer.isStopped()) {
                    urlPlayer.play();
                    //timers
                    BotLogger.log("[Internal]"," Playing SC track: " + suffix + " on channel: " + event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel());
                }
                else if (urlPlayer!=null)
                    urlPlayer.restart();
                else {
                    BotLogger.logErr(BotLogger.ERROR,"Invalid URLPlayer.");
                    event.getTextChannel().sendMessage("Error: Unable to resolve player.");
                }
            }
            try {
                soundTimer.schedule(new KillQueue(), calcLength(args[1]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int calcLength(String args) throws java.io.IOException{
        String suffix = args;
        int length;
        URLConnection temp = new URL("http://api.soundcloud.com/resolve.json?url=" + suffix + "&client_id=" + sckey).openConnection();
        HttpURLConnection tempCon = (HttpURLConnection) temp;
        String urlString = tempCon.getHeaderField("location");
        BotLogger.log(BotLogger.INTERNAL," Response code:" + tempCon.getResponseCode());
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

    public static URLPlayer getUrlPlayer() {
        return urlPlayer;
    }

    //TODO: Slight problem with this method, when called it will wipe all audio from all channels.
    public void stop(MessageReceivedEvent event) {
        event.getGuild().getAudioManager().closeAudioConnection();
        soundTimer.cancel();
    }

}
