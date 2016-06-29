package commands;

import com.sun.deploy.util.ArrayUtil;
import commands.utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
public class Poll implements Command {
    private String responseString;
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            String optionsString = "";
            for (int i = 2; i < args.length; i++) {
                if (i!=args.length-1)
                    optionsString += "\""+args[i]+"\",";
                else optionsString += "\""+args[i]+"\"";

            }
            HttpPost request = new HttpPost("https://strawpoll.me/api/v2/polls");
            StringEntity params = new StringEntity("{\n" +
                    "   \"title\": \"" + args[1] + "\",\n" +
                    "   \"options\": [\n" +
                    optionsString+
                    "   ],\n" +
                    "   \"multi\": false\n" +
                    "}");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");


        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            int startindex = responseString.indexOf("id:")+1;
            int endindex = responseString.indexOf(",");
            System.out.println(responseString);
            System.out.println(startindex+" "+endindex);
            if (responseString!=null)
                /// FIXME: 6/28/2016 this gives a string index out of bounds exception
                event.getTextChannel().sendMessage("Poll is up at www.strawpoll.me/"+responseString.substring(startindex,endindex));

        }
    }
}
