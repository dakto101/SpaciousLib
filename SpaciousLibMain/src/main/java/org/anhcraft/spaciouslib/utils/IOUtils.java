package org.anhcraft.spaciouslib.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class IOUtils {
    /**
     * Converts the given input stream into string
     * @param inputStream input stream object
     * @return string
     */
    public static String toString(InputStream inputStream){
        Scanner s = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static byte[] toByteArray(InputStream inputStream) {
        try {
            byte[] data = new byte[1024];
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            while((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            return buffer.toByteArray();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
}
