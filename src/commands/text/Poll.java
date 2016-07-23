package commands.text;
import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public class Poll implements Command {
    private int id;
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        try {
            JSONObject json = jsonEncode(args);
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost request = new HttpPost("https://strawpoll.me/api/v2/polls");
                StringEntity params = new StringEntity(json.toString(), "UTF-8");
                params.setContentType("application/json");
                request.setEntity(params);
                JSONObject response = new JSONObject(httpClient.execute(request));
                System.out.println("RESPONSE: " + response.toString());
                id = response.getInt("id");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            event.getTextChannel().sendMessage("Poll is up at https://www.strawpoll.me/" + id);
        }
    }
    private JSONObject jsonEncode(String[] args){
        JSONObject obj = new JSONObject();
        obj.put("title", args[1]);
        for (int i = 2; i < args.length; i++) {
            obj.accumulate("options", args[i]);
        }
        obj.put("multi", false);
        obj.put("dupcheck", "normal");
        obj.put("captcha", false);
        return obj;
    }
}
