package org.anhcraft.spaciouslib.socket;

public interface ServerSocketHandler {
    /**
     * This method will be called if there is a new request from a specific client.
     * @param client the manager for the connection
     * @param data the sent data
     */
    void request(ServerSocketClientManager client, byte[] data);

    /**
     * This method will be called if there is a new connection from a specific client.
     * @param client the manager for the connection
     */
    void connect(ServerSocketClientManager client);
}
