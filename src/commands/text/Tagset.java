package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Command;
import utils.FileIO;
import utils.GuildManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tagset implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        new Thread(()->{
            Guild g = event.getGuild();
            JSONObject obj = new JSONObject(GuildManager.getConfig(g));
            JSONArray arr = obj.getJSONArray("tags");
            Iterator<Object> it = arr.iterator();
            String str = "";
            while (it.hasNext()){
                str += it.next().toString().split(":",2)[0]+"\n";
            }
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost post = new HttpPost("http://pastebin.com/api/api_post.php");
            List<NameValuePair> params = new ArrayList<>(3);
            params.add(new BasicNameValuePair("api_dev_key", FileIO.readFile("paste.secret").trim()));
            params.add(new BasicNameValuePair("api_option", "paste"));
            params.add(new BasicNameValuePair("api_paste_name", "Tags for GuildID: "+ g.getId()));
            params.add(new BasicNameValuePair("api_paste_expire_date", "10M"));
            params.add(new BasicNameValuePair("api_paste_code",str));
            try {
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                HttpResponse response = httpclient.execute(post);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream in = entity.getContent();
                    try {
                        String msg = convertStreamToString(in);
                        event.getTextChannel().sendMessage(msg);
                    } finally {
                        in.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }).run();
    }

    @Override
    public String level() {
        return "Everyone";
    }

    @Override
    public String getDescription() {
        return "Shows a list of tags. Expires after 10 minutes.";
    }
    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
