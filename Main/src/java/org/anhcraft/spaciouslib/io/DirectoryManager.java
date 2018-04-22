package org.anhcraft.spaciouslib.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * A class helps you to manage directories
 */
public class DirectoryManager {
    private File directory;

    /**
     * Creates a new DirectoryManager instance
     * @param directory a directory
     */
    public DirectoryManager(File directory){
        this.directory = directory;
        if(this.directory.exists() && !this.directory.isDirectory()){
            try {
                throw new Exception("The file object doesn't represents for a directory");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a new DirectoryManager instance
     * @param path the path of a directory
     */
    public DirectoryManager(String path){
        this.directory = new File(path);
        if(this.directory.exists() && !this.directory.isDirectory()){
            try {
                throw new Exception("The file object doesn't represents for a directory");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates this directory if it doesn't exist
     * @return this object
     */
    public DirectoryManager mkdir(){
        if(!this.directory.exists()){
            this.directory.mkdir();
        }
        return this;
    }

    /**
     * Creates this directory and its parent directories if them don't exist
     * @return this object
     */
    public DirectoryManager mkdirs(){
        if(!this.directory.exists()){
            this.directory.mkdirs();
        }
        return this;
    }

    /**
     * Deletes this directory and its child directories if it exists.<br>
     * That will also affect all files of them
     * @return this object
     */
    public DirectoryManager delete(){
        if(this.directory.exists()){
            this.directory.delete();
        }
        return this;
    }

    /**
     * Copies this directory its child directories into another directory.<br>
     * That will also affect all files of them
     * @param output an output directory
     * @return this object
     */
    public DirectoryManager copy(File output) throws IOException {
        handleCopy(this.directory, output, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        });
        return this;
    }

    /**
     * Copies this directory its child directories into another directory.<br>
     * That will also affect all files of them
     * @param output an output directory
     * @param filter a file filter
     * @return this object
     */
    public DirectoryManager copy(File output, FileFilter filter) throws IOException {
        if(output.exists() && !output.isDirectory()){
            try {
                throw new Exception("The output file object doesn't represents for a directory");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        mkdir();
        new DirectoryManager(output).mkdir();
        handleCopy(this.directory, output, filter);
        return this;
    }

    private static void handleCopy(File directory, File output, FileFilter filter) throws IOException {
        // gets list of files in that directory
        for(File f : directory.listFiles(filter)){
            // gets the child output file object which is located at that output directory
            // and named same as the name of this file
            File out = new File(output, f.getName());
            // checks whether this file object represents for a normal file
            if(f.isFile()){
                // copies this file to the child output file
                // the child output file will be created automatically if it doesn't exist
                new FileManager(f).copy(out);
            }
            // checks whether this file object represents for a directory
            else if(f.isDirectory()){
                // creates the child output directory
                new DirectoryManager(out).mkdir();
                // calls this method again which is handled in the child output directory
                handleCopy(f, out, filter);
            }
        }
    }

    /**
     * Cleans all the files and directories inside this directory
     * @return this object
     */
    public DirectoryManager clean(){
        for(File file : this.directory.listFiles()){
            file.delete();
        }
        return this;
    }
}
