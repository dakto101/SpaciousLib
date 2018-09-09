package org.anhcraft.spaciouslib.placeholder;

import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * A cached placeholder will store the value of the placeholder for each online player.<br>
 * So if you replace the cached placeholder, you won't get the newest data immediately.<br>
 * By default, the cached placeholder will renew every 10 seconds.
 */
public abstract class CachedPlaceholder extends Placeholder {
    @PlayerCleaner
    protected LinkedHashMap<UUID, String> cache = new LinkedHashMap<>();

    protected void updateCache(){
        this.cache = new LinkedHashMap<>();
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            updateCache(player);
        }
    }

    protected void updateCache(Player player){
        if(player != null) {
            String str = getValue(player);
            if(str != null) {
                this.cache.put(player.getUniqueId(), str);
            } else {
                this.cache.put(player.getUniqueId(), "");
            }
        }
    }

    protected String getCache(Player player){
        return cache.get(player.getUniqueId());
    }
}
