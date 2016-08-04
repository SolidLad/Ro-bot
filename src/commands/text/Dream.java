package commands.text;

import utils.Command;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
public class Dream implements Command {
    @Override
    public void run(MessageReceivedEvent event, String[] args) {
        new Thread(() ->{
            event.getTextChannel().sendMessage("Ｉ ｄｉｄ ｎＡＵＧＨＴｔ！ Ｍｉｓｔｅｒ Ｅｌｅｃｔｉｃ ｓｅｎｄ ｈｉｍ ｔｏ ｔｈｅ ｐｒｉｎｃｉｐａｌ＇ｓ ｏｆｆｉｃｅ ａｎｄ ｈａｖｅ ｈｉｍ ＥＸｐｅｌＬｅｄ！\uFEFF https://www.youtube.com/watch?v=CAtDt_qjQ4o");
        }).run();
    }
    @Override
    public String level() {
        return "Everyone";
    }
    public String getDescription()
    {
        return "Prints a funny message  USAGE: **dream";
    }
}
