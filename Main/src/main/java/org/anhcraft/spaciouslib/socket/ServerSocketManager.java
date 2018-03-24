package org.anhcraft.spaciouslib.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

    public ServerSocketManager sendAll(String data) throws IOException {
        for(ServerSocketClientHandler c : clients){
            c.send(data);
        }
        return this;
    }

    public void close() throws IOException {
        this.isStopped = true;
        this.interrupt();
        for(ServerSocketClientHandler c : clients){
            c.close();
        }
        socket.close();
        clients.clear();
    }

    public List<ServerSocketClientHandler> getClients(){
        return this.clients;
    }

    @Override
    public void run() {
        try {
            while(true) {
                if(this.isStopped){
                    break;
                }
                Socket client = socket.accept();
                if(client != null) {
                    ServerSocketClientHandler c = new ServerSocketClientHandler(this, client, requestHandler);
                    clients.add(c);
                    c.start();
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
