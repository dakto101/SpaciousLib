package org.anhcraft.spaciouslib.socket;

import org.anhcraft.spaciouslib.builders.EqualsBuilder;
import org.anhcraft.spaciouslib.builders.HashCodeBuilder;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A class helps you to manage the connections between a socket client and a server socket.<br>
 * This class is for the client side.
 */
public class ClientSocketManager extends SocketHandler {
    private Socket server;
    private ClientSocketHandler requestHandler;
    private List<byte[]> data;

    /**
     * Creates a new client socket and starts a new thread for handling the requests.
     * @param address the IP address or hostname of a socket server
     * @param port the TCP/IP port which is listening by a socket server
     * @param requestHandler a handler for this client socket
     */
    public ClientSocketManager(String address, int port, ClientSocketHandler requestHandler){
        this.requestHandler = requestHandler;
        try{
            server = new Socket(address, port);
        } catch(Exception e){
            e.printStackTrace();
        }
        this.data = new ArrayList<>();
        try {
            this.in = server.getInputStream();
            this.out = server.getOutputStream();
        } catch(IOException e) {
            e.printStackTrace();
        }
        this.isStopped = false;
        // starts the thread
        this.start();
    }

    /**
     * Closes this client socket.
     */
    public void close() throws IOException {
        this.isStopped = true;
        this.interrupt();
        out.close();
        in.close();
        server.close();
    }

    /**
     * Gets a list of data.<br>
     * Each data is of each time the server sent
     * @return list of data
     */
    public List<byte[]> getData(){
        return this.data;
    }

    @Override
    public void run() {
        try {
            while(!this.isStopped){
                if(0 < this.in.available()) {
                    byte[] data = new byte[1024];
                    this.in.read(data);
                    this.requestHandler.response(this, data);
                    this.data.add(data);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            ClientSocketManager h = (ClientSocketManager) o;
            return new EqualsBuilder()
                    .append(h.server.getInetAddress(), this.server.getInetAddress())
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(27, 13)
                .append(this.server.getInetAddress()).build();
    }
}
