package org.anhcraft.spaciouslib.placeholder;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * A cached placeholder will store the value of the placeholder for each online player.<br>
 * So if you replace the cached placeholder, you won't get the newest data immediately.<br>
 * By default, the cached placeholder will renew every 10 seconds.
 */
public abstract class CachedPlaceholder extends Placeholder {
    protected LinkedHashMap<UUID, String> cache = new LinkedHashMap<>();

    protected void updateCache(){
        this.cache = new LinkedHashMap<>();
        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
            updateCache(player);
        }
    }

    protected void updateCache(ProxiedPlayer player){
        this.cache.put(player.getUniqueId(), getValue(player));
    }

    protected String getCache(ProxiedPlayer player){
        return cache.get(player.getUniqueId());
    }
}
