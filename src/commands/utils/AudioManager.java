package commands.utils;

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
import java.util.*;

public class AudioManager
{
    private String sckey = FileIO.readFile("stuff2.gitignore");
    private Timer soundTimer = new Timer();
    private static URLPlayer urlPlayer;
    private static long end;
    private long totalTime = 0;
    HashMap<Integer, String> song = new HashMap<Integer, String>();
    ArrayList<Integer> keys1 = new ArrayList<Integer>();

    public synchronized void addSong(MessageReceivedEvent event, String[] args)
    {
        totalTime = 0;

        if(song.size() == 0)
        {
            try
            {
                //length of song
                int dur = calcLength(args[1]);
                //puts the song and duration in hashmap
                song.put(dur, args[1]);
                //adds duration for the key index
                keys1.add(dur);
                //plays song
                playNext(event, args[1]);
                totalTime += dur;
                end = (new Date().getTime()) + dur;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        else
        {
            try
            {
                //calculates song duration
                int dur = calcLength(args[1]);
                
                boolean check = true;
                while(check)
                {
                    for(int i = 0; i < keys1.size(); i++)
                    {
                        if(keys1.get(i) == dur)
                        {
                            dur += 1;
                            check = true;
                            break;
                        }
                        else
                        {
                            check = false;
                        }
                    }
                }
                
                
                //puts song in hashmap
                song.put(dur, args[1]);
                //puts duration in key array
                keys1.add(dur);
                //grabs time
                long temptime = new Date().getTime();
                totalTime = end - temptime;

                for (int i = 1; i < song.size(); i++)
                {
                    totalTime += keys1.get(i);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }

        soundTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println(song.size());
                song.remove(keys1.get(0));
                keys1.remove(0);
                if(song.size() == 0)
                {
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
                else
                {
                    System.out.println("testing");
                    playNext(event, song.get(keys1.get(0)));
                    end += keys1.get(0);
                }
            }
        }, totalTime);
    }


    private void playNext(MessageReceivedEvent event, String args){
        if (args!=null) {
            String suffix = args;
            URL audioUrl = null;
            try {
                URL trackUrl = new URL(suffix);
                URLConnection con1 = trackUrl.openConnection();
                //open a url to get the location.
                URLConnection temp = new URL("http://api.soundcloud.com/resolve.json?url=" + suffix + "&client_id=" + sckey).openConnection();
                HttpURLConnection tempCon = (HttpURLConnection) temp;
                //get the redirect url.
                String urlString = tempCon.getHeaderField("location");
                BotLogger.log(BotLogger.mediumTimestamp,"[Internal]"," Response code:" + tempCon.getResponseCode());
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
                    BotLogger.log("[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "]","[Internal]"," Playing SC track: " + suffix + " on channel: " + event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel());
                }
                else if (urlPlayer!=null)
                    urlPlayer.restart();
                else {
                    BotLogger.logErr(BotLogger.mediumTimestamp,BotLogger.ERROR,"Invalid URLPlayer.");
                    event.getTextChannel().sendMessage("Error: Unable to resolve player.");
                }
            }
        }
    }

    private int calcLength(String args) throws java.io.IOException{
        String suffix = args;
        int length;
        URLConnection temp = new URL("http://api.soundcloud.com/resolve.json?url=" + suffix + "&client_id=" + sckey).openConnection();
        HttpURLConnection tempCon = (HttpURLConnection) temp;
        String urlString = tempCon.getHeaderField("location");
        BotLogger.log(BotLogger.mediumTimestamp,BotLogger.INTERNAL," Response code:" + tempCon.getResponseCode());
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


    public static URLPlayer getUrlPlayer() {
        return urlPlayer;
    }

}
