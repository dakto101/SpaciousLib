package org.anhcraft.spaciouslib.socket;

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
    private ServerSocketRequestHandler requestHandler;
    protected List<ServerSocketClientManager> clients;
    private boolean isStopped;

    /**
     * Creates a new server socket and starts a new thread for handling the requests.
     * @param port the TCP/IP port which is listening by this socket server
     * @param requestHandler a handler for the socket connections
     */
    public ServerSocketManager(int port, ServerSocketRequestHandler requestHandler){
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
     * Sends the given content to all clients.
     * @param content the content as string
     * @return this object
     */
    public ServerSocketManager sendAll(String content) throws IOException {
        for(ServerSocketClientManager c : clients){
            c.send(content);
        }
        return this;
    }

    /**
     * Closes this server socket and all current socket connections.
     */
    public void close() throws IOException {
        this.isStopped = true;
        this.interrupt();
        for(ServerSocketClientManager c : clients){
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
            while(true) {
                if(this.isStopped || this.socket.isClosed()){
                    break;
                }
                Socket client = socket.accept();
                if(client != null) {
                    ServerSocketClientManager c = new ServerSocketClientManager(this, client, requestHandler);
                    clients.add(c);
                }
            }
        } catch(Exception ignored){ }
    }
}
