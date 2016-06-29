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
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class Poll implements Command {
    private String responseString;
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            String optionsString = "";
            for (int i = 2; i < args.length; i++) {
                if (i!=args.length-1)
                    optionsString += "\""+args[i]+"\",\n";
                else optionsString += "\""+args[i]+"\"";

            }
            HttpPost request = new HttpPost("https://strawpoll.me/api/v2/polls");
            StringEntity params = new StringEntity("{\n" +
                    "   \"title\": \"" + args[1] + "\",\n" +
                    "   \"options\": [\n" +
                    optionsString+
                    "   \n],\n" +
                    "   \"multi\": false\n" +
                    "}");
            request.addHeader("content-type", "application/json");
            System.out.println(EntityUtils.toString(params));
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            responseString = convertStreamToString(entity.getContent(),"UTF-8");


        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            int startindex = responseString.indexOf("id:")+1;
            int endindex = responseString.indexOf(",");
            System.out.println(responseString);
            System.out.println(startindex+" "+endindex);
            if (responseString!=null)
                /// FIXME: 6/28/2016 this gives a string index out of bounds exception. think the issue is with the post request.
                event.getTextChannel().sendMessage("Poll is up at www.strawpoll.me/"+responseString.substring(startindex,endindex));

        }
    }
    public static String convertStreamToString(InputStream is, String encoding ) throws IOException{
        StringBuilder sb = new StringBuilder( Math.max( 16, is.available() ) );
        char[] tmp = new char[ 4096 ];

        try {
            InputStreamReader reader = new InputStreamReader( is, encoding );
            for( int cnt; ( cnt = reader.read( tmp ) ) > 0; )
                sb.append( tmp, 0, cnt );
        } finally {
            is.close();
        }
        return sb.toString();
    }
}
