package org.anhcraft.spaciouslib.socket;

public interface ClientSocketRequestHandler {
    void request(ClientSocketManager manager, String data);
}
