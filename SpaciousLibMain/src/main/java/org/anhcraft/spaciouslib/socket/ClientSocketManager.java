package org.anhcraft.spaciouslib.socket;

import org.anhcraft.spaciouslib.builders.EqualsBuilder;
import org.anhcraft.spaciouslib.builders.HashCodeBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A class helps you to manage the connections between a socket client and a server socket.<br>
 * This class is for the client side.
 */
public class ClientSocketManager extends SocketHandler {
    private Socket server;
    private ClientSocketHandler requestHandler;

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
        try {
            this.in = new BufferedInputStream(server.getInputStream());
            this.out = new BufferedOutputStream(server.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
        // starts the thread
        this.start();
    }

    /**
     * Closes this client socket.
     */
    public void close() throws IOException {
        this.interrupt();
        server.close();
        out.close();
        in.close();
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
            while(this.isAlive() && !server.isClosed() && (n = this.in.read()) != -1){
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
                        this.requestHandler.response(this, buffer.toByteArray());
                        buffer.reset();
                        // prevent duplicates
                        triggered = false;
                    }
                }
            }
            // happen when the data wasn't read completely
            if(triggered) {
                buffer.flush();
                this.requestHandler.response(this, buffer.toByteArray());
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
