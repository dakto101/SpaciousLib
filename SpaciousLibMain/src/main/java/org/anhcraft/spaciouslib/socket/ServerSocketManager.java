package org.anhcraft.spaciouslib.socket;

import org.anhcraft.spaciouslib.builders.EqualsBuilder;
import org.anhcraft.spaciouslib.builders.HashCodeBuilder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A class helps you to manage the connections between a server socket and multiple socket clients.<br>
 * This class is for server side
 */
public class ServerSocketManager extends Thread {
    private ServerSocket socket;
    private ServerSocketHandler requestHandler;
    protected List<ServerSocketClientManager> clients;
    private boolean isStopped;

    /**
     * Creates a new server socket and starts a new thread for handling the requests.
     * @param port the TCP/IP port which is listening by this socket server
     * @param requestHandler a handler for the server socket
     */
    public ServerSocketManager(int port, ServerSocketHandler requestHandler){
        this.requestHandler = requestHandler;
        clients = new ArrayList<>();
        try{
            socket = new ServerSocket(port);
        } catch(Exception e){
            e.printStackTrace();
        }
        this.start();
    }

    /**
     * Closes this server socket and all current socket connections.
     */
    public void close() throws IOException {
        this.isStopped = true;
        this.interrupt();
        List<ServerSocketClientManager> a = new ArrayList<>(clients); // clones
        for(ServerSocketClientManager c : a){
            c.close();
        }
        socket.close();
        clients.clear();
    }

    /**
     * Gets all clients which were connected to this server.
     * @return list of socket connection manager between this server and a specific client
     */
    public List<ServerSocketClientManager> getClients(){
        return this.clients;
    }

    @Override
    public void run() {
        try {
            while(this.isAlive() && !this.isStopped && !this.socket.isClosed()) {
                Socket client = socket.accept();
                ServerSocketClientManager c = new ServerSocketClientManager(this, client, requestHandler);
                requestHandler.connect(c);
                clients.add(c);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            ServerSocketManager h = (ServerSocketManager) o;
            return new EqualsBuilder()
                    .append(h.clients, this.clients)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(35, 51)
                .append(this.clients).build();
    }

    public boolean isStopped() {
        return isStopped;
    }
}
