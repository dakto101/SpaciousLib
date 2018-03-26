package org.anhcraft.spaciouslib.socket;

/**
 * Represents a socket request implementation.
 */
public interface ServerSocketRequestHandler {
    /**
     * This method will be called if there is a new request from a specific client.
     * @param client ServerSocketClientHandler object
     * @param data the data which was sent by that client
     */
    void request(ServerSocketClientHandler client, String data);
}
