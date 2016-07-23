package utils;

import net.dv8tion.jda.entities.Guild;

import java.io.*;

public class FileIO {
    public static String readFile(String fileName){
        String contents = "";
        try {
            File file = new File(fileName);
            if(!file.exists()) {
                return contents;
            }
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                contents += line + "\n";
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }
    public static void writeLog(String path, String msg) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(path),true));
            writer.write(msg+"\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}