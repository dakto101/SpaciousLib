package org.anhcraft.spaciouslib.files;

import java.io.*;
import java.util.List;

public class UTF8FileWriter {

    /**
     * Writes a UTF-8 file
     *
     * @param strs a specific list of lines
     * @param f file which you want to write
     *
     */
    public UTF8FileWriter(List<String> strs, File f) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(f), "UTF8"));
        for(String s : strs) {
            out.write(s+"\n");
        }
        out.flush();
        out.close();
    }

    /**
     * Writes a UTF-8 file
     *
     * @param str a specific string
     * @param f file which you want to write
     *
     */
    public UTF8FileWriter(String str, File f) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(f), "UTF8"));
        out.write(str);
        out.flush();
        out.close();
    }
}
