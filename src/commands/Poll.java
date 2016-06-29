package commands;
import commands.utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;;
import org.json.JSONObject;

public class Poll implements Command {
    private int id = 1;
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        try {
            JSONObject json = jsonEncode(args);

            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                HttpPost request = new HttpPost("http://strawpoll.me/api/v2/polls");
                StringEntity params = new StringEntity(json.toString());
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                JSONObject response = new JSONObject(httpClient.execute(request));
                id = response.getInt("id");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            event.getTextChannel().sendMessage("Poll is up at https://www.strawpoll.me/"+id);
        }
    }
    private JSONObject jsonEncode(String[] args){
        JSONObject obj = new JSONObject();
        obj.put("title", args[1]);
        for (int i = 2; i < args.length; i++) {
            obj.append("options", args[i]);
        }
        obj.put("multi", false);
        System.out.println(obj);
        return obj;
    }
}
