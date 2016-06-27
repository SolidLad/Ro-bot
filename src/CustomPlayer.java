import net.dv8tion.jda.audio.player.URLPlayer;
import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alex on 6/27/2016.
 */
public class CustomPlayer {
    private MessageReceivedEvent event;
    private URLPlayer urlPlayer;
    private Timer soundTimer;
    public CustomPlayer(MessageReceivedEvent event, URLPlayer urlPlayer, Timer soundTimer){
        this.event = event;
        this.urlPlayer = urlPlayer;
        this.soundTimer = soundTimer;
    }
    public void playSound(){
        String suffix;
        if (event.getMessage().getContent().length()>5)
            suffix = event.getMessage().getContent().substring(6);
        else suffix = null;
        URL audioUrl = null;
        urlPlayer = null;
        int length = 1000*300;
        int channel = Integer.parseInt(suffix.substring(suffix.length()-1));
        suffix = suffix.substring(0,suffix.length()-2);
        try {
            URL trackUrl = new URL(suffix);
            URLConnection con1 = trackUrl.openConnection();
            HttpURLConnection connection1 =(HttpURLConnection) con1;
            URLConnection temp = new URL("http://api.soundcloud.com/resolve.json?url="+suffix+"&client_id="+BotMain.client_id).openConnection();
            HttpURLConnection tempCon = (HttpURLConnection) temp;
            String urlString = tempCon.getHeaderField("location");
            System.out.println("["+ LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "][Internal] Response code:"+tempCon.getResponseCode());
            System.out.println("["+ LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + "][Internal] Playing SC track " +suffix+" on channel:"+event.getGuild().getVoiceChannels().get(channel-1));
            audioUrl = new URL("http://api.soundcloud.com/tracks/"+urlString.substring(34,43)+"/stream?client_id="+BotMain.client_id);
            HttpURLConnection.setFollowRedirects(false);
            URLConnection con2 = audioUrl.openConnection();
            InputStream in = new URL("http://api.soundcloud.com/tracks/"+urlString.substring(34,43)+"?client_id="+BotMain.client_id).openStream();
            int i;
            char c;
            String data = "";
            try{
                while((i=in.read())!=-1) {
                    c=(char)i;
                    data = data + String.valueOf(c);
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                System.out.println(data);
                if(in!=null)
                    in.close();
            }
            length = Integer.parseInt(data.substring(data.indexOf("duration\":")+10, data.indexOf(",\"commentable\"")));
            HttpURLConnection connection2 =(HttpURLConnection) con2;
            audioUrl = new URL(con2.getHeaderField("Location"));
            urlPlayer = new URLPlayer(event.getJDA(),audioUrl);
            ((HttpURLConnection) con1).disconnect();
            ((HttpURLConnection) temp).disconnect();
            ((HttpURLConnection) con2).disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (audioUrl!=null) {
            event.getGuild().getAudioManager().setSendingHandler(urlPlayer);
            event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannels().get(channel-1));
            if (!urlPlayer.isStopped())
                urlPlayer.play();
            else urlPlayer.restart();
            soundTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            }, length + 1000);
        }


    }


}
