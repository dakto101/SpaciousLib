package org.anhcraft.spaciouslib.socket;

public interface ClientSocketHandler {
    /**
     * This method will be called if there is a new response from a socket server.
     * @param manager the manager of the connection
     * @param data the sent data
     */
    void response(ClientSocketManager manager, byte[] data);
}
