package org.anhcraft.spaciouslib.io;

import java.io.File;

/**
 * A class helps you to manage the given directory
 */
public class DirectoryManager {
    private File directory;

    /**
     * Creates a new DirectoryManager instance
     * @param directory the File object
     */
    public DirectoryManager(File directory){
        this.directory = directory;
    }

    /**
     * Creates a new DirectoryManager instance
     * @param path the path of the directory
     */
    public DirectoryManager(String path){
        this.directory = new File(path);
    }

    /**
     * Creates that directory and parent directories (if necessary) if it doesn't exist
     * @return this object
     */
    public DirectoryManager mkdirs(){
        if(!this.directory.exists()){
            this.directory.mkdirs();
        }
        return this;
    }

    /**
     * Deletes that directory if it exists
     * @return this object
     */
    public DirectoryManager delete(){
        if(this.directory.exists()){
            this.directory.delete();
        }
        return this;
    }
}
