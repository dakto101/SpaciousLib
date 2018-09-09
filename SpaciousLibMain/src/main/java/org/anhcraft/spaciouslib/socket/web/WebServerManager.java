package org.anhcraft.spaciouslib.socket.web;

import org.anhcraft.spaciouslib.socket.ServerSocketHandler;
import org.anhcraft.spaciouslib.socket.ServerSocketManager;

/**
 * A class helps you to manage the connections between a web server and multiple clients.<br>
 * This class is for server side
 */
public class WebServerManager extends ServerSocketManager {
    /**
     * Creates a new web server and starts a new thread for handling the requests.
     * @param requestHandler a handler for the server
     */
    public WebServerManager(ServerSocketHandler requestHandler) {
        super(80, requestHandler);
    }
}
