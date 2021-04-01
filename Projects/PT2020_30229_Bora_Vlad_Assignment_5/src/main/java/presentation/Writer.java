package presentation;

import java.io.*;

public class Writer {


    public static void write(String fileName, String text)
    {
        try {
            File logFile = new File(fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(text);
            writer.close();
        }catch (Exception e)
        {

        }
    }
}
