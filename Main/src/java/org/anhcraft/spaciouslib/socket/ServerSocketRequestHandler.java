package org.anhcraft.spaciouslib.socket;

public interface ServerSocketRequestHandler {
    /**
     * This method will be called if there is a new request from a specific client.
     * @param client the manager which was received the response, represents for a socket connection between a server socket and a socket client
     * @param content the sent content
     */
    void request(ServerSocketClientManager client, String content);
}
