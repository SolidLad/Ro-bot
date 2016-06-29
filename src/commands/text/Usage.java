package commands.text;

import commands.utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Usage implements Command{
    private Map<String, String> commandDescriptions = new HashMap<>();
    private String[] commands = {"ban","deafen","dream","help","kick","mute","patrick","play","poll","roll","say","stop","unban","undeafen","unmute","usage","users"};
    private String[] descriptions = {"Bans a user. Usage: ```!ban <Username> <Days>```","Deafens a user. Usage: ```!deafen <Username>```",
            "Links a youtube video. Usage: ```!dream```", "Displays a list of commands Usage: ```!help```","Kicks a user. Usage: ```!kick <Username>```","Mutes a user. Usage: ```!mute <Username>```",
            "Plays one of patrick's sounds. Usage: ```!patrick <Sound>```","Plays a soundcloud track. Usage: ```!play <Track URL> <int Channel> <Float volume>```",
            "Creates a poll with up to twenty options. Usage: ```!poll <Title>  <Option1> <Option2> <Option3> etc.","Rolls a six sided die or a die with a specified number of sides. Usage: ```!roll <Sides>```", "Makes the bot say something. Usage: ```!say <Message>```","Stops the currently play track and clears the queue.  Usage: ```!stop```",
            "Unbans a user. Usage: ```!unban <Username>```", "Undeafens a user. Usage: ```!undeafen <Username>```", "Unmutes a user. Usage: ```!unmute <Username>```",
            "Displays a command description and usage. Usage: ```!usage <Command>```", "Prints a list of users and their online statuses. Usage: ```!users```"
    };
    public Usage(){
        for (int i = 0; i < commands.length; i++) {
            commandDescriptions.put(commands[i], descriptions[i]);
        }
    }
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        if (commandDescriptions.get(args[1])!=null)
            event.getTextChannel().sendMessage(commandDescriptions.get(args[1]));
        else event.getTextChannel().sendMessage("Command not recognized. Please enter commands in all lowercase letters.");

    }
}//
