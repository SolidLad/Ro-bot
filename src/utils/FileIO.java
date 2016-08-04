package utils;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

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
            File file = new File(path);
            file.getParentFile().mkdirs();
            if (file.createNewFile())
                BotLogger.log(BotLogger.INFO, "log was created at: "+ path);
            writer = new BufferedWriter(new FileWriter(file,true));
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
    public static int calcLines(String filePath) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filePath));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean endsWithoutNewLine = false;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n')
                        ++count;
                }
                endsWithoutNewLine = (c[readChars - 1] != '\n');
            }
            if(endsWithoutNewLine) {
                ++count;
            }
            return count;
        } finally {
            is.close();
        }
    }
}