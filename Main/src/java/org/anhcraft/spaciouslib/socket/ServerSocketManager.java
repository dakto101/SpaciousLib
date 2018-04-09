package org.anhcraft.spaciouslib.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A class helps you to manage current server socket.
 */
public class ServerSocketManager extends Thread {
    private ServerSocket socket;
    private ServerSocketRequestHandler requestHandler;
    protected List<ServerSocketClientHandler> clients;
    private boolean isStopped;

    public ServerSocketManager(int port, ServerSocketRequestHandler requestHandler){
        this.requestHandler = requestHandler;
        clients = new ArrayList<>();
        try{
            socket = new ServerSocket(port);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Sends a new data to all clients.
     * @param data the data in string
     * @return this object
     */
    public ServerSocketManager sendAll(String data) throws IOException {
        for(ServerSocketClientHandler c : clients){
            c.send(data);
        }
        return this;
    }

    /**
     * Closes the current thread and all socket connections.
     */
    public void close() throws IOException {
        this.isStopped = true;
        this.interrupt();
        for(ServerSocketClientHandler c : clients){
            c.close();
        }
        socket.close();
        clients.clear();
    }

    /**
     * Gets all clients which were connected to this server.
     * @return list of ServerSocketClientHandler object
     */
    public List<ServerSocketClientHandler> getClients(){
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
                    ServerSocketClientHandler c = new ServerSocketClientHandler(this, client, requestHandler);
                    clients.add(c);
                    c.start();
                }
            }
        } catch(Exception ignored){ }
    }
}
