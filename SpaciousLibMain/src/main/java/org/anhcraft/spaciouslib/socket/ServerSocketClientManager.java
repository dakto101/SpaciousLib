package org.anhcraft.spaciouslib.socket;

import org.anhcraft.spaciouslib.builders.EqualsBuilder;
import org.anhcraft.spaciouslib.builders.HashCodeBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * A class helps you to manage the connections between a server socket and a socket client.<br>
 * This class is for server side and only for a specific client
 */
public class ServerSocketClientManager extends SocketHandler {
    private Socket client;
    private ServerSocketManager manager;
    private ServerSocketHandler requestHandler;

    /**
     * Gets the manager of this server socket
     * @return ServerSocketManager object
     */
    public ServerSocketManager getManager(){
        return this.manager;
    }

    protected ServerSocketClientManager(ServerSocketManager manager, Socket client, ServerSocketHandler requestHandler) {
        this.requestHandler = requestHandler;
        this.manager = manager;
        this.client = client;
        try {
            this.in = new BufferedInputStream(client.getInputStream());
            this.out = new BufferedOutputStream(client.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
        this.start();
    }

    /**
     * Gets the address of this client
     * @return the address
     */
    public InetAddress getInetAddress(){
        return this.client.getInetAddress();
    }

    @Override
    public void run() {
        try {
            // init the buffer
            ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
            int n;
            int csize = 0;
            int size = -1;
            boolean triggered = false;
            while(this.isAlive() && manager.isAlive() && !manager.isStopped() && !client.isClosed()
                    && (n = this.in.read()) != -1){
                // read the size of data first
                if(size == -1){
                    size = n;
                    triggered = true;
                }
                else {
                    // write each byte to the output
                    buffer.write(n);
                    // increase the current size of data by 1
                    csize++;
                    // when read the data completely
                    // reset and then wait for the next data
                    if(csize == size){
                        size = -1;
                        csize = 0;
                        buffer.flush();
                        this.requestHandler.request(this, buffer.toByteArray());
                        buffer.reset();
                        // prevent duplicates
                        triggered = false;
                    }
                }
            }
            // happen when the data wasn't read completely
            if(triggered) {
                buffer.flush();
                this.requestHandler.request(this, buffer.toByteArray());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes this socket connection.
     */
    public void close() throws IOException {
        this.interrupt();
        manager.clients.remove(this);
        client.close();
        out.close();
        in.close();
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            ServerSocketClientManager h = (ServerSocketClientManager) o;
            return new EqualsBuilder()
                    .append(h.client.getInetAddress(), this.client.getInetAddress())
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(25, 33)
                .append(this.client.getInetAddress()).build();
    }
}
