package org.anhcraft.spaciouslib.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class helps you to manage all sockets which are created by this library.<br>
 * Can works stably on a Spigot server and interacted by asynchronous tasks.<br>
 * <br>
 * A socket is one endpoint of a two-way communication link between two programs running on the network. A socket is bound to a port number so that the TCP layer can identify the application that data is destined to be sent to (from <a href="https://docs.oracle.com/javase/tutorial/networking/sockets/definition.html">https://docs.oracle.com/javase/tutorial/networking/sockets/definition.html</a>).
 */
public class SocketManager {
    private static List<ServerSocketManager> servers = new ArrayList<>();
    private static List<ClientSocketManager> clients = new ArrayList<>();

    /**
     * Registers a new server socket which listens on the given port.<br>
     * This method also creates a new thread for that server socket
     * @param port the port which this server listens on
     * @param requestHandler the ServerSocketRequestHandler object
     * @return a ServerSocketManager object
     */
    public static ServerSocketManager registerServer(int port, ServerSocketRequestHandler requestHandler){
        ServerSocketManager manager = new ServerSocketManager(port, requestHandler);
        servers.add(manager);
        manager.start();
        return manager;
    }

    /**
     * Registers a new socket client which connects to a specific socket server.<br>
     * This method also creates a new thread for that socket client
     * @param address the IP address or hostname of that server socket
     * @param port the port of that server socket
     * @param requestHandler the ClientSocketRequestHandler object
     * @return a ClientSocketManager object
     */
    public static ClientSocketManager registerClient(String address, int port, ClientSocketRequestHandler requestHandler){
        ClientSocketManager manager = new ClientSocketManager(address, port, requestHandler);
        clients.add(manager);
        manager.start();
        return manager;
    }

    /**
     * Unregisters a specific server socket from the given ServerSocketManager object.
     * @param manager ServerSocketManager object
     * @throws IOException
     */
    public static void unregisterServer(ServerSocketManager manager) throws IOException {
        manager.close();
        servers.remove(manager);
    }

    /**
     * Unregisters a specific socket client from the given ClientSocketManager object.
     * @param manager ClientSocketManager object
     * @throws IOException
     */
    public static void unregisterClient(ClientSocketManager manager) throws IOException {
        manager.close();
        clients.remove(manager);
    }

    /**
     * Unregisters all socket.
     */
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
