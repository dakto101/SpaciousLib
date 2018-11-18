package org.anhcraft.spaciouslib.socket;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class SocketHandler extends Thread {
    protected BufferedInputStream in;
    protected BufferedOutputStream out;
    public abstract void close() throws IOException;

    public BufferedOutputStream getOutput(){
        return this.out;
    }

    public BufferedInputStream getInput(){
        return this.in;
    }

    public void send(byte[] data) throws IOException {
        if(data == null || data.length == 0){
            return;
        }
        this.out.write(data.length);
        this.out.write(data);
        this.out.flush();
    }

    public void send(String data) throws IOException {
        if(data == null || data.length() == 0){
            return;
        }
        byte[] str = data.getBytes(StandardCharsets.UTF_8);
        this.out.write(str.length);
        this.out.write(str);
        this.out.flush();
    }
}