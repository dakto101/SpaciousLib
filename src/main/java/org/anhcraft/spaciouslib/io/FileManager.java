package org.anhcraft.spaciouslib.io;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileManager {
    private File file;

    public FileManager(File file){
        this.file = file;
    }

    public void create(){
        try {
            this.file.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(){
        this.file.delete();
    }

    public void delete(long delaySeconds){
        new BukkitRunnable() {
            @Override
            public void run() {
                file.delete();
            }
        }.runTaskLaterAsynchronously(SpaciousLib.instance, 20 * delaySeconds);
    }

    public void write(byte[] data, boolean append) throws IOException {
        FileOutputStream stream = new FileOutputStream(this.file, append);
        stream.write(data);
        stream.flush();
        stream.close();
    }

    public void write(byte[] data) throws IOException {
        write(data, false);
    }

    public void writeAsString(String string) throws IOException {
        write(string.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] read() throws IOException {
        byte[] data = new byte[(int) this.file.length()];
        FileInputStream stream = new FileInputStream(this.file);
        stream.read(data);
        stream.close();
        return data;
    }

    public String readAsString() throws IOException {
        return new String(read());
    }

    public void cleanUTF8BOM() throws IOException {
        String s = readAsString();
        if (s.startsWith("\uFEFF")) {
            s = s.substring(1);
        }
        writeAsString(s);
    }
}
