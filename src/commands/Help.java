package commands;

import commands.utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Created by jackbachman on 6/27/16.
 * Let me Commit
 */
public class Help implements Command {

    private final String commandCodeBlock =
            "```!roll                                            | rolls a six sided die.\n" +
                    "!users                                         | shows a list of users and their online status.\n" +
                    "!help                                          | shows a list of commands\n" +
                    "!patrick <sound>                               | plays a sound that patrick once made. '!patrick ?' will print the list of sounds.\n"+
                    "!test                                          | for debugging. does nothing useful as of now.\n" +
                    "!D R E A M                                     | links a youtube video\n" +
                    "!say <message>                                 | sends a message in the general channel.\n" +
                    "!play <url> <channel number><float volume>     | plays a song (currently only supports soundcloud)\n" +
                    "!stop                                          | stops the player and clears the queue\n" +
                    "!kick <username>                               | kicks target user. Use target's username and not their nick. Case sensitive.\n" +
                    "!ban <username> <days>```                      | bans target user for <days> days.. Use target's username and not their nick. Case sensitive.\n";

    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        String prefix;
        prefix = event.getAuthor().getAsMention();
        event.getTextChannel().sendMessage(prefix + " A list of commands and their functions has been sent to your private messages.");
        event.getAuthor().getPrivateChannel().sendMessage(commandCodeBlock);
    }
}
