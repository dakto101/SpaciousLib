package org.anhcraft.spaciouslib.socket;

public interface ServerSocketRequestHandler {
    void request(ServerSocketClientHandler client, String data);
}
