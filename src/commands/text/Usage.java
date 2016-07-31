package commands.text;
import exceptions.MalformedCommandException;
import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.*;

public class Usage implements Command
{

    private List<Command> commandList = new ArrayList<Command>();

    Map<String, String> commandsAndDescriptions = new HashMap<String, String>();

    private String[] commands = {"Ban", "ChannelInfo", "ClearChannel", "CreatePoll", "Deafen", "Dream", "EndPoll", "Help", "Info", "Kick", "Mute", "Patrick", "Pause", "Play", "Playlist", "Queue", "Record", "Restart", "Resume", "Roll", "Say", "ServerInfo", "Shuffle", "Shutdown", "Skip", "Stats", "Stop", "Unban", "Undeafen", "Unmute", "commands.text.Usage", "Users", "UserInfo", "Volume", "Vote"};

    private File commandFolder = new File("src/commands");

    @Override
    public void run(MessageReceivedEvent event, String[] args) throws MalformedCommandException
    {
        readFolder(commandFolder, "");

        for (int i = 0; i < commands.length; i++)
        {
            try
            {
                boolean hasInstanceFields = false;
                String description = commandList.get(i).getDescription();

                String commandName = "";

                //this section is trying to grab the command name, in order to pair each description with each command name
                try
                {
                    //if it has parameters it will have a "<" character
                    commandName = description.substring(description.indexOf(">")+2, description.indexOf("<")-1);
                    //this boolean prevents the variable from changing in the next line
                    hasInstanceFields = true;
                }
                catch(Exception e)
                {
                   //if the command doesn't have parameters, it will throw an exception, due to not having a "<" character, giving a negative index
                }
                //prevents double assigning of the commandName variable
                if(!hasInstanceFields)
                {
                    //functions that are not shown or are for debugging won't have a ">" as there is no usage example
                    if(description.contains(">"))
                    {
                        //this is for commands that have no parameters
                        commandName = description.substring(description.indexOf(">")+2);
                    }
                }

                //making sure we aren't adding empty debug or testing commands
                if(!commandName.equals(""))
                {
                    for (String command : commands)
                    {
                        //matches a command with its description
                        if ((command.toLowerCase()).equals(commandName))
                        {
                            commandsAndDescriptions.put(command, description);
                        }
                    }
                }
            }
            catch(Exception e)
            {
                //meh, this should work
            }
        }

        //once the hashmap of commands and descriptions is filled, the memory of the commandList is cleared
        commandList.clear();

        String message = "";
        //checking if the user provides the correct command syntax
        if(args.length > 1)
        {
            message = createResponse(commandsAndDescriptions, args);
        }

        //checks for null message and sends if not null
        if (!message.equals(""))
        {
            event.getTextChannel().sendMessage("`"+message+"`");
        }
        //otherwise gives invalid usage command
        else
        {
            event.getTextChannel().sendMessage("Invalid usage command. Usage: `>>usage <Command>`\nTry `>>help` for a list of commands.");
        }

        commandsAndDescriptions.clear();
    }

    public String createResponse(Map<String, String> map, String[] args)
    {
        String answer = "";

        for(String key : map.keySet())
        {
            if (key.equalsIgnoreCase(args[1].toLowerCase()))
            {
                answer += key + "\t";
                answer += map.get(key) + "\n";
                break;
            }
        }

        return answer;
    }

    public String getDescription()
    {
        return "Returns usages of commands";
    }

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
                    commandList.add(tempCommand);

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
