package org.anhcraft.spaciouslib.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SocketManager {
    private static List<ServerSocketManager> servers = new ArrayList<>();
    private static List<ClientSocketManager> clients = new ArrayList<>();

    public static ServerSocketManager registerServer(int port, ServerSocketRequestHandler requestHandler){
        ServerSocketManager manager = new ServerSocketManager(port, requestHandler);
        servers.add(manager);
        manager.start();
        return manager;
    }

    public static ClientSocketManager registerClient(String address, int port, ClientSocketRequestHandler requestHandler){
        ClientSocketManager manager = new ClientSocketManager(address, port, requestHandler);
        clients.add(manager);
        manager.start();
        return manager;
    }

    public static void unregisterServer(ServerSocketManager manager) throws IOException {
        manager.close();
        servers.remove(manager);
    }

    public static void unregisterClient(ClientSocketManager manager) throws IOException {
        manager.close();
        clients.remove(manager);
    }

    public static void unregisterAll(){
        for(ClientSocketManager c : clients) {
            try {
                c.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        for(ServerSocketManager s : servers) {
            try {
                s.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        servers = new ArrayList<>();
        clients = new ArrayList<>();
    }
}
