package commands.text;
import exceptions.MalformedCommandException;
import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.*;

public class Usage implements Command
{

    private List<Command> desccommand = new ArrayList<Command>();

    Map<String, String> commandsAndDescriptions = new HashMap<String, String>();

    private String[] commands = {"Ban", "ChannelInfo", "ClearChannel", "CreatePoll", "Deafen", "Dream", "EndPoll", "Help", "Info", "Kick", "Mute", "Patrick", "Pause", "Play", "Playlist", "Queue", "Record", "Restart", "Resume", "Roll", "Say", "ServerInfo", "Shuffle", "Shutdown", "Skip", "Stats", "Stop", "Unban", "Undeafen", "Unmute", "commands.text.Usage", "Users", "UserInfo", "Volume", "Vote"};

    //folder of commands
    private File cmdfolder = new File("src/commands");

    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException {

            readFolder(cmdfolder, "");
            for (int i = 0; i < commands.length; i++)
            {
                try
                {
                    boolean test = false;
                    String description = desccommand.get(i).getDescription();
                    String s = "";
                    try
                    {
                        s = description.substring(description.indexOf(">")+2, description.indexOf("<")-1);
                        test = true;
                    }
                    catch(Exception e)
                    {
                    }
                    try
                    {
                        if(!test)
                        {
                            if(description.contains(">"))
                            {
                                s = description.substring(description.indexOf(">")+2);
                            }
                        }
                    }
                    catch(Exception e)
                    {
                    }

                    if(!s.equals(""))
                    {
                        for (String command : commands) {
                            if ((command.toLowerCase()).equals(s)) {
                                commandsAndDescriptions.put(command, description);
                            }
                        }
                    }
                }
                catch(Exception e)
                {
                }
            }


        desccommand.clear();
        String message;
        if(args.length == 2)
        {
            message = createAns(commandsAndDescriptions, args);
        }
        else
        {
            message = "Invalid Command";
            for (String key : commandsAndDescriptions.keySet())
            {
                if(key.toLowerCase().equals(args[1].toLowerCase()))
                {
                    message = key + "\t" + commandsAndDescriptions.get(key);
                    break;
                }
            }

        }
        if (!message.equals(""))
            event.getTextChannel().sendMessage("`"+message+"`");
        else event.getTextChannel().sendMessage("Invalid usage command. Usage: `>>usage <Command>`\nTry `>>help` for a list of commands.");
        commandsAndDescriptions.clear();
    }

    public String createAns(Map<String, String> map, String[] args) throws MalformedCommandException {
        String answer = "";


        for(String key : map.keySet())
        {
            if (key.equalsIgnoreCase(args[1].toLowerCase())) {
                answer += key + "\t";
                answer += map.get(key) + "\n";
            }
        }
        if (answer.equals("``"))
            throw new MalformedCommandException();
        return answer;
    }

    public String getDescription()
    {
        return "Returns usages of commands";
    }


    //just copied and pasted from CommandHandler... It works I guess....

    private void readFolder(File commandFolder, String parentFolderChain)
    {
        for(File subFile: commandFolder.listFiles())
        {
            // Make sure it is not a directory and make sure that it is a java class.
            if(subFile.isFile() && subFile.toString().substring(subFile.toString().lastIndexOf(".") + 1).equals("java"))
            {
                try
                {
                    Class<?> tempClass = Class.forName(parentFolderChain + "." + commandFolder.getName() + "." + subFile.getName().substring(0,subFile.getName().indexOf(".")));
                    Command tempCommand = (Command) tempClass.newInstance();
                    desccommand.add(tempCommand);

                }
                catch (ClassNotFoundException | IllegalAccessException | InstantiationException e)
                {
                    e.printStackTrace();
                }

            }

            else if(subFile.isDirectory() /*&& !isBanned(subFile)*/)
            {
                readFolder(subFile, parentFolderChain + commandFolder.getName());
            }
        }
    }

}
