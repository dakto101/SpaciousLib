package org.anhcraft.spaciouslib.io;

import java.io.File;

public class DirectoryManager {
    private File file;

    public DirectoryManager(File file){
        this.file = file;
    }

    public DirectoryManager(String path){
        this.file = new File(path);
    }

    public DirectoryManager mkdirs(){
        if(!this.file.exists()){
            this.file.mkdirs();
        }
        return this;
    }

    public DirectoryManager delete(){
        if(this.file.exists()){
            this.file.delete();
        }
        return this;
    }
}
