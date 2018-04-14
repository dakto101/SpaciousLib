package org.anhcraft.spaciouslib.socket;

public interface ClientSocketRequestHandler {
    /**
     * This method will be called if there is a new response from a socket server.
     * @param manager the manager of the client socket which was received the response
     * @param content the sent content
     */
    void response(ClientSocketManager manager, String content);
}
