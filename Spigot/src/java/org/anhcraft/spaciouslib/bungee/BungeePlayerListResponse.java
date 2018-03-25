package org.anhcraft.spaciouslib.bungee;

import java.util.List;

public abstract class BungeePlayerListResponse extends BungeeResponse {
    public abstract void result(String server, List<String> players);
}