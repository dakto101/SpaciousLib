package org.anhcraft.spaciouslib.socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * A class helps you to manage the connections between a socket client and a server socket.<br>
 * This class is for the client side.
 */
public class ClientSocketManager extends SocketHandler {
    private Socket server;
    private ClientSocketRequestHandler requestHandler;

    /**
     * Creates a new client socket and starts a new thread for handling the requests.
     * @param address the IP address or hostname of a socket server
     * @param port the TCP/IP port which is listening by a socket server
     * @param requestHandler a handler for this socket connection
     */
    public ClientSocketManager(String address, int port, ClientSocketRequestHandler requestHandler){
        this.requestHandler = requestHandler;
        try{
            server = new Socket(address, port);
        } catch(Exception e){
            e.printStackTrace();
        }
        try {
            this.in = new InputStreamReader(server.getInputStream());
            this.out = new OutputStreamWriter(server.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
        this.isStopped = false;
        // starts the thread
        this.start();
    }

    /**
     * Sends the given content to the server
     * @param content the content as string
     * @return this object
     */
    public ClientSocketManager send(String content) throws IOException {
        out.write(content);
        out.flush();
        return this;
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

    @Override
    public void run() {
        Scanner scan = new Scanner(this.in);
        while(scan.hasNext()){
            if(this.isStopped || this.server.isClosed()){
                break;
            }
            requestHandler.response(this, scan.nextLine());
        }
    }
}
