package org.anhcraft.spaciouslib.socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocketManager extends SocketHandler {
    private Socket server;
    private ClientSocketRequestHandler requestHandler;

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
    }

    public ClientSocketManager send(String data) throws IOException {
        out.write(data + "\n");
        out.flush();
        return this;
    }

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
            requestHandler.request(this, scan.nextLine());
        }
    }
}
