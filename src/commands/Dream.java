package commands;

import commands.utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Created by jackbachman on 6/27/16.
 */
public class Dream implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        event.getTextChannel().sendMessage("Ｉ ｄｉｄ ｎＡＵＧＨＴｔ！ Ｍｉｓｔｅｒ Ｅｌｅｃｔｉｃ ｓｅｎｄ ｈｉｍ ｔｏ ｔｈｅ ｐｒｉｎｃｉｐａｌ＇ｓ ｏｆｆｉｃｅ ａｎｄ ｈａｖｅ ｈｉｍ ＥＸｐｅｌＬｅｄ！\uFEFF https://www.youtube.com/watch?v=CAtDt_qjQ4o");
    }
}
