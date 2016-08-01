package utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
public class CommandHandler {

    private String commandFolderPath = "src/commands";
    private String[] bannedFolders = new String[] {"utils", "blacklist"};
    private File commandFolder;
    public static Map<String, Command> commands = new HashMap<>();

    public CommandHandler() {
        commandFolder = new File(commandFolderPath);
        if(commandFolder.isDirectory()) {
            readFolder(commandFolder, "");
        }
    }

    private void readFolder(File commandFolder, String parentFolderChain) {
        for(File subFile: commandFolder.listFiles()) {
            // Make sure it is not a directory and make sure that it is a java class.
            if(subFile.isFile() && subFile.toString().substring(subFile.toString().lastIndexOf(".") + 1).equals("java")) {
                try {
                    Class<?> tempClass = Class.forName(parentFolderChain + "." + commandFolder.getName() + "." + subFile.getName().substring(0,subFile.getName().indexOf(".")));
                    Command tempCommand = (Command) tempClass.newInstance();
                    commands.put("**"+subFile.getName().toLowerCase().substring(0, subFile.getName().indexOf(".")), tempCommand);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if(subFile.isDirectory() && !isBanned(subFile)) {
                readFolder(subFile, parentFolderChain + commandFolder.getName());
            }
        }
    }

    private boolean isBanned(File subFile) {
        for(int i = 0; i < bannedFolders.length; i++) {
            if(subFile.getName().equals(bannedFolders[i])) {
                return true;
            }
        }
        return false;
    }
}