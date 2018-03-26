package org.anhcraft.spaciouslib.socket;

/**
 * Represents a socket response implementation.
 */
public interface ClientSocketRequestHandler {
    /**
     * This method will be called if there is a new response from the server.
     * @param manager ClientSocketManager object
     * @param data the data which was sent by that server
     */
    void response(ClientSocketManager manager, String data);
}
