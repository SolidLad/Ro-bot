package commands.text;
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
    public void run(MessageReceivedEvent event, String[] args)
    {

            readFolder(cmdfolder, "");
            for (int i = 0; i < commands.length; i++)
            {
                try
                {
                    boolean test = false;
                    String x = desccommand.get(i).getDescription();
                    String s = "";
                    try
                    {
                        s = x.substring(x.indexOf(">")+2, x.indexOf("<")-1);
                        test = true;
                    }
                    catch(Exception e)
                    {
                    }
                    try
                    {
                        if(test == false)
                        {
                            if(x.indexOf(">") != -1)
                            {
                                s = x.substring(x.indexOf(">")+2);
                            }
                        }
                    }
                    catch(Exception e)
                    {
                    }

                    if(!s.equals(""))
                    {
                        for (int j = 0; j < commands.length; j++)
                        {
                            if((commands[j].toLowerCase()).equals(s))
                            {
                                commandsAndDescriptions.put(commands[j], x);
                            }
                        }
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }


        desccommand.clear();
        String message;
        if(args.length == 1)
        {
            message = createAns(commandsAndDescriptions);
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
        event.getTextChannel().sendMessage(message);
        commandsAndDescriptions.clear();
    }

    public String createAns(Map x)
    {
        String answer = "";


        for(String key : commandsAndDescriptions.keySet())
        {
            answer += key + "\t";
            answer += commandsAndDescriptions.get(key) + "\n";
        }

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
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (InstantiationException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
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
