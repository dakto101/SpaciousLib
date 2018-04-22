package org.anhcraft.spaciouslib.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class SocketHandler extends Thread {
    protected boolean isStopped;
    protected InputStream in;
    protected OutputStream out;
    public abstract void close() throws IOException;

    public OutputStream getOutput(){
        return this.out;
    }

    public InputStream getInput(){
        return this.in;
    }

    public void send(byte[] data) throws IOException {
        this.out.write(data);
        this.out.flush();
    }

    public void send(String data) throws IOException {
        this.out.write(data.getBytes(StandardCharsets.UTF_8));
        this.out.flush();
    }
}