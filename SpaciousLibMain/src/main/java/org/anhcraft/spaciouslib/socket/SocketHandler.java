package org.anhcraft.spaciouslib.socket;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class SocketHandler extends Thread {
    protected boolean isStopped;
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
        this.out.write(data);
        this.out.flush();
    }

    public void send(String data) throws IOException {
        if(data.charAt(data.length()-1) != '\n'){
            data = data + "\n";
        }
        this.out.write(data.getBytes(StandardCharsets.UTF_8));
        this.out.flush();
    }
}