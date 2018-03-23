package org.anhcraft.spaciouslib.bungee;

import java.util.UUID;

public abstract class BungeePlayerUUIDResponse extends BungeeResponse {
    public abstract void result(UUID uuid);
}