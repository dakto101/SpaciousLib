package org.anhcraft.spaciouslib.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileManager {
    private File file;

    public FileManager(File file){
        this.file = file;
    }

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

    public FileManager createAndWrite(byte[] data) throws IOException {
        if(!this.file.exists()){
            return create().writeIfExist(data);
        }
        return this;
    }

    /**
     * Writes a content to that file
     * @param data content in byte array
     * @param append does that content append at the end of the file?
     * @throws IOException
     */
    public FileManager write(byte[] data, boolean append) throws IOException {
        FileOutputStream stream = new FileOutputStream(this.file, append);
        stream.write(data);
        stream.flush();
        stream.close();
        return this;
    }

    /**
     * Writes a new content to that file
     * @param data content in byte array
     * @throws IOException
     */
    public FileManager write(byte[] data) throws IOException {
        return write(data, false);
    }

    /**
     * Writes a new content to that file
     * @param string content in string
     * @throws IOException
     */
    public FileManager writeAsString(String string) throws IOException {
        return write(string.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Writes a content to that file if it exists
     * @param data content in byte array
     * @param append does that content append at the end of the file?
     * @throws IOException
     */
    public FileManager writeIfExist(byte[] data, boolean append) throws IOException {
        if(this.file.exists()) {
            FileOutputStream stream = new FileOutputStream(this.file, append);
            stream.write(data);
            stream.flush();
            stream.close();
        }
        return this;
    }

    /**
     * Writes a new content to that file if it exists
     * @param data content in byte array
     * @throws IOException
     */
    public FileManager writeIfExist(byte[] data) throws IOException {
        if(this.file.exists()) {
            return write(data, false);
        } else {
            return this;
        }
    }

    /**
     * Writes a new content to that file if it exists
     * @param string content in string
     * @throws IOException
     */
    public FileManager writeAsStringIfExist(String string) throws IOException {
        if(this.file.exists()){
            return write(string.getBytes(StandardCharsets.UTF_8));
        } else {
            return this;
        }
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
    public FileManager cleanUTF8BOM() throws IOException {
        String s = readAsString();
        if (s.startsWith("\uFEFF")) {
            s = s.substring(1);
        }
        return writeAsString(s);
    }
}
