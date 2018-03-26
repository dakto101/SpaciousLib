package org.anhcraft.spaciouslib.placeholder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

/**
 * A cached placeholder will store the value of the placeholder for each online player.<br>
 * So if you replace the cached placeholder, you won't get the newest data immediately.<br>
 * By default, the cached placeholder will renew every 10 seconds.
 */
public abstract class CachedPlaceholder extends Placeholder {
    protected LinkedHashMap<String, String> cache;

    protected void updateCache(){
        this.cache = new LinkedHashMap<>();
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            updateCache(player);
        }
    }

    protected void updateCache(Player player){
        this.cache.put(player.getName(), getValue(player));
    }

    protected String getCache(Player player){
        return cache.get(player.getName());
    }
}
