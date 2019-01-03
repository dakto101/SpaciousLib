package org.anhcraft.spaciouslib.placeholder;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.anhcraft.spaciouslib.annotations.PlayerCleaner;

import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * CachedPlaceholder stores the placeholder value which will increase the performance.<br>
 * Each placeholder get changes after specified duration which was set in the configuration.
 */
public abstract class CachedPlaceholder extends Placeholder {
    @PlayerCleaner
    private LinkedHashMap<UUID, String> cache = new LinkedHashMap<>();

    void updateCache(){
        this.cache.clear();
        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
            updateCache(player);
        }
    }

    void updateCache(ProxiedPlayer player){
        this.cache.put(player.getUniqueId(), getValue(player));
    }

    String getCache(ProxiedPlayer player){
        return cache.get(player.getUniqueId());
    }
}
