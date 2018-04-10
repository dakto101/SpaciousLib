package org.anhcraft.spaciouslib.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A class helps you to manage the specified file
 */
public class FileManager {
    private File file;

    /**
     * Creates a new FileManager instance
     * @param file represents the file as a File object
     */
    public FileManager(File file){
        this.file = file;
    }

    /**
     * Creates a new FileManager instance
     * @param path the path of the file
     */
    public FileManager(String path){
        this.file = new File(path);
    }

    /**
     * Creates the specified file if it doesn't exist
     * @return this object
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
     * Deletes the specified file if it exists
     * @return this object
     */
    public FileManager delete(){
        if(this.file.exists()) {
            this.file.delete();
        }
        return this;
    }

    /**
     * Initializes the specified file if it doesn't exist (creates and writes the given data)
     * @param data an array of bytes (binary data)
     * @return this object
     */
    public FileManager initFile(byte[] data) throws IOException {
        if(!this.file.exists()){
            return create().writeIfExist(data);
        }
        return this;
    }

    /**
     * Writes the given data to the specified file
     * @param data an array of bytes (binary data)
     * @param append does that data append at the end of the file
     * @return this object
     */
    public FileManager write(byte[] data, boolean append) throws IOException {
        FileOutputStream stream = new FileOutputStream(this.file, append);
        stream.write(data);
        stream.flush();
        stream.close();
        return this;
    }

    /**
     * Writes the given data to the specified file (Warning: the old data will lost)
     * @param data an array of bytes (binary data)
     * @return this object
     */
    public FileManager write(byte[] data) throws IOException {
        return write(data, false);
    }

    /**
     * Writes the given data to the specified file if it exist
     * @param data an array of bytes (binary data)
     * @param append does that data append at the end of the file
     * @return this object
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
     * Writes the given data to the specified file if it exist (Warning: the old data will lost)
     * @param data an array of bytes (binary data)
     * @return this object
     */
    public FileManager writeIfExist(byte[] data) throws IOException {
        if(this.file.exists()) {
            return write(data, false);
        } else {
            return this;
        }
    }

    /**
     * Reads the specified file
     * @return the content as an array of bytes (binary data)
     */
    public byte[] read() throws IOException {
        byte[] data = new byte[(int) this.file.length()];
        FileInputStream stream = new FileInputStream(this.file);
        stream.read(data);
        stream.close();
        return data;
    }

    /**
     * Reads the specified file
     * @return the content as a string
     */
    public String readAsString() throws IOException {
        return new String(read());
    }
}
