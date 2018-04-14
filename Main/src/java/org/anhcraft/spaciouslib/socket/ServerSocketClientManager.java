package org.anhcraft.spaciouslib.socket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * A class helps you to manage the connections between a server socket and a socket client.<br>
 * This class is for server side and only for a specific client
 */
public class ServerSocketClientManager extends SocketHandler {
    private Socket client;
    private ServerSocketManager manager;
    private ServerSocketRequestHandler requestHandler;

    /**
     * Gets the manager of this server socket
     * @return ServerSocketManager object
     */
    public ServerSocketManager getManager(){
        return this.manager;
    }

    /**
     * Sends the given content to this client
     * @param content the content as string
     * @return this object
     */
    public ServerSocketClientManager send(String content) throws IOException {
        out.write(content);
        out.flush();
        return this;
    }

    protected ServerSocketClientManager(ServerSocketManager manager, Socket client, ServerSocketRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        this.manager = manager;
        this.client = client;
        try {
            this.in = new InputStreamReader(client.getInputStream());
            this.out = new OutputStreamWriter(client.getOutputStream());
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
        Scanner scan = new Scanner(this.in);
        while(scan.hasNext()){
            if(this.isStopped || this.client.isClosed()){
                break;
            }
            requestHandler.request(this, scan.nextLine());
        }
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
}
