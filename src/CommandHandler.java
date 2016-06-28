import commands.utils.Command;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jackbachman on 6/27/16.
 */
public class CommandHandler {

    private String commandFolderPath = "src/commands";
    private File commandFolder;
    Map<String, Command> commands = new HashMap<>();


    public CommandHandler() {
        commandFolder = new File(commandFolderPath);
        if(commandFolder.isDirectory()) {
            for(File subFile: commandFolder.listFiles()) {
                // Make sure it is not a directory and make sure that it is a java class.
                if(subFile.isFile() && subFile.toString().substring(subFile.toString().lastIndexOf(".") + 1).equals("java")) {
                    try {
                        Class<?> tempClass = Class.forName(commandFolder.getName() + "." + subFile.getName().substring(0,subFile.getName().indexOf(".")));
                        Command tempCommand = (Command) tempClass.newInstance();
                        commands.put("!"+subFile.getName().toLowerCase().substring(0, subFile.getName().indexOf(".")), tempCommand);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
