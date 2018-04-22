package org.anhcraft.spaciouslib.socket;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A class helps you to manage the connections between a server socket and a socket client.<br>
 * This class is for server side and only for a specific client
 */
public class ServerSocketClientManager extends SocketHandler {
    private Socket client;
    private ServerSocketManager manager;
    private ServerSocketHandler requestHandler;
    private List<byte[]> data;

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
        this.data = new ArrayList<>();
        try {
            this.in = client.getInputStream();
            this.out = client.getOutputStream();
        } catch(IOException e) {
            e.printStackTrace();
        }
        this.isStopped = false;
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
            while(!this.isStopped){
                if(0 < this.in.available()) {
                    byte[] data = new byte[1024];
                    this.in.read(data);
                    this.requestHandler.request(this, data);
                    this.data.add(data);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of data.<br>
     * Each data is of each time the client sent
     * @return list of data
     */
    public List<byte[]> getData(){
        return this.data;
    }

    /**
     * Closes this socket connection.
     */
    public void close() throws IOException {
        this.isStopped = true;
        this.interrupt();
        out.close();
        in.close();
        client.close();
        manager.clients.remove(this);
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
        return new HashCodeBuilder(28, 12)
                .append(this.client.getInetAddress()).toHashCode();
    }
}
