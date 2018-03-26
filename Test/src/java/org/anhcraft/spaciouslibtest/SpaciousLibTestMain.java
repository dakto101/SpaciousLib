package org.anhcraft.spaciouslibtest;

import org.anhcraft.spaciouslib.socket.ClientSocketManager;
import org.anhcraft.spaciouslib.socket.ClientSocketRequestHandler;
import org.anhcraft.spaciouslib.socket.SocketManager;

import java.io.IOException;
import java.util.Scanner;

public class SpaciousLibTestMain {
    public static void main(String[] args){
        ClientSocketManager c = SocketManager.registerClient("localhost", 25568, new ClientSocketRequestHandler() {
            @Override
            public void response(ClientSocketManager manager, String data) {}
        });
        Scanner s = new Scanner(System.in);
        while(s.hasNextLine()){
            try {
                c.send(s.nextLine());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        try {
            c.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}