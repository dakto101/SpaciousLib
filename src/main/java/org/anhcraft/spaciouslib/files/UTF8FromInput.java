package org.anhcraft.spaciouslib.files;

import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UTF8FromInput {
    private InputStream f;
    private StringBuilder s = new StringBuilder();

    /**
     * Reads a UTF-8 resource
     *
     * @param c main class
     * @param path path of the resource
     *
     */
    public UTF8FromInput(Class c, String path){
        this.f = c.getResourceAsStream(path);
        a();
    }

    /**
     * Reads a UTF-8 resource inside the jar file of a Bukkit plugin
     *
     * @param p Bukkit plugin
     * @param path path of the resource
     *
     */
    public UTF8FromInput(Plugin p, String path){
        this.f = p.getClass().getResourceAsStream(path);
        a();
    }


    private void newLine(){
        if(0 < s.length()){
            s.append("\n");
        }
    }

    private void a(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(f, "UTF8"));
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
