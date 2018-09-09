package org.anhcraft.spaciouslib.bungee;

import java.util.UUID;

public abstract class BungeeOtherPlayerUUIDResponse extends BungeeResponse {
    public abstract void result(String player, UUID uuid);
}