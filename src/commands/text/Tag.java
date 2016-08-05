package commands.text;

import exceptions.MalformedCommandException;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Command;
import utils.GuildManager;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Tag implements Command {
    // TODO: 8/3/2016 needs a rename and possibly a rewrite
    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {
        new Thread(()-> {
            Guild g = event.getGuild();
            JSONObject obj = new JSONObject(GuildManager.getConfig(g));
            List<String> tags = new ArrayList<>();
            if (!args[1].equals("-c") && !args[1].equals("-d")){
                String str;
                if (obj.get("tags") instanceof String) {
                    str = (String) obj.get("tags");
                }
                else {
                    JSONArray arr = obj.getJSONArray("tags");
                    for (Object string : arr) {
                        tags.add(string.toString());
                    }
                }
                String[] tagArray = tags.toArray(new String[0]);
                for (String tagset : tagArray) {
                    String[] tag = tagset.split(":", 2);
                    if (tag[0].equals(args[1])){
                        event.getTextChannel().sendMessage(tag[1]);
                        return;
                    }
                }
                event.getTextChannel().sendMessage("Tag "+args[1]+" not found.");
            }
            else if (args[1].equals("-c")){
                if (obj.get("tags").toString().contains(args[2]+":")){
                    event.getTextChannel().sendMessage("Tag already exists with that key");
                    return;
                }
                obj.accumulate("tags",args[2]+":"+compileString(args));
                String name = "guild\\config\\"+g.getId();
                File file = new File(name);
                    try {
                        FileWriter writer = new FileWriter(file);
                        writer.write(obj.toString());
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                event.getTextChannel().sendMessage("Tag "+"\""+args[2]+"\" was created.");
            }
            else if (args[1].equals("-d")){
                String str ="";
                if (obj.get("tags") instanceof String){
                    str = ((String) obj.get("tags"));
                    str = StringUtils.remove(str, str.substring(str.indexOf(args[2]+":"),str.indexOf("\"", str.indexOf(args[2]+":"))));
                    System.out.println(str);
                    Iterator<Object> iterator = obj.getJSONArray("tags").iterator();
                    while(iterator.hasNext()){
                        String string = ((String) iterator.next());
                        if (string.substring(0,args[2].length()).equals(args[2])){
                            iterator.remove();
                        }

                    }
                }
                else {
                    JSONArray arr = ((JSONArray) obj.get("tags"));
                    str = arr.toString();
                    str = StringUtils.remove(str, str.substring(str.indexOf(args[2]+":"),str.indexOf("\"", str.indexOf(args[2]+":"))));
                    System.out.println(str);
                    Iterator<Object> iterator = obj.getJSONArray("tags").iterator();
                    while(iterator.hasNext()){
                        String string = ((String) iterator.next());
                        if (string.length() >= args[2].length() && string.substring(0, args[2].length()).equals(args[2])) {
                            iterator.remove();
                        }

                    }
                }
                String name = "guild\\config\\"+g.getId();
                File file = new File(name);
                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write(obj.toString());
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                event.getTextChannel().sendMessage("Tag "+"\""+args[2]+"\" was deleted.");
            }

        }).run();
    }

    @Override
    public String level() {
        return "Everyone";
    }
    private String compileString(String[] args)  {
        if(args.length >= 3) {
            String finalString = "";
            for (int i = 3; i < args.length; i++) {
                if (i!=args.length-1)
                    finalString += args[i] + " ";
                else finalString += args[i];
            }
            return finalString;
        }
        return null;
    }
    @Override
    public String getDescription() {
        return "Tags a message for future reuse. Usage: `**tag -c <Name> <Message>` to create a tag, `**tag <Name>` to use a tag or `**tag -d <Name>` to delete a tag. Note: Tag currently only supports the UTF-8 charset.";
    }
}
