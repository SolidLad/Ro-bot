package commands.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by jackbachman on 6/27/16.
 */
public class FileIO {
    public static String readStuff(String fileName){
        try {
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            StringBuffer stringBuffer = new StringBuffer();
            int numCharsRead;
            char[] charArray = new char[1024];
            while ((numCharsRead = fileReader.read(charArray)) > 0) {
                stringBuffer.append(charArray, 0, numCharsRead);
            }
            fileReader.close();
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
