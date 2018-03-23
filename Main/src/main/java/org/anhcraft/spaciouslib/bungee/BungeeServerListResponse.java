package org.anhcraft.spaciouslib.bungee;

import java.util.List;

public abstract class BungeeServerListResponse extends BungeeResponse {
    public abstract void result(List<String> servers);
}