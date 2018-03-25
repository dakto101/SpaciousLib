package org.anhcraft.spaciouslib.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileManager {
    private File file;

    /**
     * Creates a new FileManager instance<br>
     * Mustn't be a directory
     * @param file a file
     */
    public FileManager(File file){
        this.file = file;
    }

    /**
     * Creates a new FileManager instance<br>
     * Mustn't be a directory
     * @param path file's path
     */
    public FileManager(String path){
        this.file = new File(path);
    }

    /**
     * Creates that file if it doesn't exist
     */
    public FileManager create(){
        if(!this.file.exists()){
            try {
                this.file.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
     * Deletes that file
     */
    public FileManager delete(){
        if(this.file.exists()) {
            this.file.delete();
        }
        return this;
    }

    /**
     * Writes a content to that file
     * @param data content in byte array
     * @param append does that content append at the end of the file?
     * @throws IOException
     */
    public void write(byte[] data, boolean append) throws IOException {
        FileOutputStream stream = new FileOutputStream(this.file, append);
        stream.write(data);
        stream.flush();
        stream.close();
    }

    /**
     * Writes a new content to that file
     * @param data content in byte array
     * @throws IOException
     */
    public void write(byte[] data) throws IOException {
        write(data, false);
    }

    /**
     * Writes a new content to that file
     * @param string content in string
     * @throws IOException
     */
    public void writeAsString(String string) throws IOException {
        write(string.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Reads that file
     * @return the content of the file in byte array
     * @throws IOException
     */
    public byte[] read() throws IOException {
        byte[] data = new byte[(int) this.file.length()];
        FileInputStream stream = new FileInputStream(this.file);
        stream.read(data);
        stream.close();
        return data;
    }

    /**
     * Reads that file
     * @return the content of the file in string
     * @throws IOException
     */
    public String readAsString() throws IOException {
        return new String(read());
    }

    /**
     * Clears all UTF-8 BOM out of that file
     * @throws IOException
     */
    public void cleanUTF8BOM() throws IOException {
        String s = readAsString();
        if (s.startsWith("\uFEFF")) {
            s = s.substring(1);
        }
        writeAsString(s);
    }
}
