package org.anhcraft.spaciouslib.socket;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SocketManager {
    private static LinkedHashMap<JavaPlugin, List<ServerSocketManager>> servers = new LinkedHashMap<>();
    private static LinkedHashMap<JavaPlugin, List<ClientSocketManager>> clients = new LinkedHashMap<>();

    public static ServerSocketManager registerServer(JavaPlugin plugin, int port, ServerSocketRequestHandler requestHandler){
        ServerSocketManager manager = new ServerSocketManager(port, requestHandler);
        List<ServerSocketManager> s = new ArrayList<>();
        if(servers.containsKey(plugin)){
            s = servers.get(plugin);
        }
        s.add(manager);
        servers.put(plugin, s);
        manager.start();
        return manager;
    }

    public static ClientSocketManager registerClient(JavaPlugin plugin, String address, int port, ClientSocketRequestHandler requestHandler){
        ClientSocketManager manager = new ClientSocketManager(address, port, requestHandler);
        List<ClientSocketManager> s = new ArrayList<>();
        if(clients.containsKey(plugin)){
            s = clients.get(plugin);
        }
        s.add(manager);
        clients.put(plugin, s);
        manager.start();
        return manager;
    }

    public static void unregisterServer(JavaPlugin plugin, ServerSocketManager manager) throws IOException {
        if(servers.containsKey(plugin)){
            return;
        }
        manager.close();
        List<ServerSocketManager> s = servers.get(plugin);
        s.remove(manager);
        servers.put(plugin, s);
    }

    public static void unregisterClient(JavaPlugin plugin, ClientSocketManager manager) throws IOException {
        if(clients.containsKey(plugin)){
            return;
        }
        manager.close();
        List<ClientSocketManager> s = clients.get(plugin);
        s.remove(manager);
        clients.put(plugin, s);
    }

    public static void unregisterAll(JavaPlugin plugin){
        if(clients.containsKey(plugin)) {
            for(ClientSocketManager c : clients.get(plugin)) {
                try {
                    c.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(servers.containsKey(plugin)) {
            for(ServerSocketManager s : servers.get(plugin)) {
                try {
                    s.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        servers.remove(plugin);
        clients.remove(plugin);
    }

    public static void unregisterAll(){
        for(List<ClientSocketManager> cm : clients.values()){
            for(ClientSocketManager c : cm){
                try {
                    c.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for(List<ServerSocketManager> sm : servers.values()){
            for(ServerSocketManager s : sm){
                try {
                    s.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        servers = new LinkedHashMap<>();
        clients = new LinkedHashMap<>();
    }
}
