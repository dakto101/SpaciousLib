package org.anhcraft.spaciouslib.utils;

import java.io.*;
import java.net.URL;
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

    /**
     * Read the given URL into a string
     * @param url an URL
     * @return string
     */
    public static String toString(URL url){
        try {
            return toString(url.openConnection().getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts the given input stream into a byte array
     * @param inputStream Input
     * @return an array of bytes
     */
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

    /**
     * Read the given URL into a byte array
     * @param url an URL
     * @return an array of bytes
     */
    public static byte[] toByteArray(URL url){
        try {
            return toByteArray(url.openConnection().getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    /**
     * Copy the data from an input to an output.<br>
     * Notice that the stream will still be opened after then
     * @param in input
     * @param out output
     */
    public static void copy(InputStream in, OutputStream out){
        try {
            BufferedInputStream ins = new BufferedInputStream(in);
            BufferedOutputStream outs = new BufferedOutputStream(out);
            byte[] data = new byte[1024];
            int nRead;
            while((nRead = ins.read(data, 0, data.length)) != -1) {
                outs.write(data, 0, nRead);
            }
            outs.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copy the data from an input to an output
     * @param in input
     * @param out output
     * @param close close the stream after finished or not
     */
    public static void copy(InputStream in, OutputStream out, boolean close){
        try {
            BufferedInputStream ins = new BufferedInputStream(in);
            BufferedOutputStream outs = new BufferedOutputStream(out);
            byte[] data = new byte[1024];
            int nRead;
            while((nRead = ins.read(data, 0, data.length)) != -1) {
                outs.write(data, 0, nRead);
            }
            outs.flush();
            if(close){
                in.close();
                out.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
