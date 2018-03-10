package org.anhcraft.spaciouslib.files;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UTF8FromFile {
    private File f;
    private StringBuilder s = new StringBuilder();

    /**
     * Reads a UTF-8 file
     *
     * @param f file which you want to read
     *
     */
    public UTF8FromFile(File f){
        this.f = f;
        a();
    }

    private void newLine(){
        if(0 < s.length()){
            s.append("\n");
        }
    }

    private void a(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));
            String str;
            while ((str = in.readLine()) != null) {
                newLine();
                s.append(UTF8BOMCleaner.a(str));
            }
            in.close();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Gets the content of that file as a string
     */
    public String b(){
        return s.toString();
    }

    /**
     * Gets the content of that file as a list of lines
     */
    public List<String> c(){
        return new ArrayList<>(Arrays.asList(s.toString().replaceAll("\r\n", "\n").split("\n")));
    }
}
