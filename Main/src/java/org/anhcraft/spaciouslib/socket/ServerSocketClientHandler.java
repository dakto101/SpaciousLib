package org.anhcraft.spaciouslib.socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerSocketClientHandler extends SocketHandler {
    private Socket client;
    private ServerSocketManager manager;
    private ServerSocketRequestHandler requestHandler;

    public ServerSocketManager getManager(){
        return this.manager;
    }

    public ServerSocketClientHandler send(String data) throws IOException {
        out.write(data + "\n");
        out.flush();
        return this;
    }

    public ServerSocketClientHandler(ServerSocketManager manager, Socket client, ServerSocketRequestHandler requestHandler) {
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
    }

    @Override
    public void run() {
        Scanner scan = new Scanner(this.in);
        while(scan.hasNext()){
            if(this.isStopped){
                break;
            }
            requestHandler.request(this, scan.nextLine());
        }
    }

    public void close() throws IOException {
        this.isStopped = true;
        this.interrupt();
        out.close();
        in.close();
        client.close();
        manager.clients.remove(this);
    }
}
