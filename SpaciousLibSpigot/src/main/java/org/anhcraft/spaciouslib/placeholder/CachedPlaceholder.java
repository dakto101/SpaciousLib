package org.anhcraft.spaciouslib.placeholder;

import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * CachedPlaceholder stores the placeholder value which will increase the performance.<br>
 * Each placeholder get changes after specified duration which was set in the configuration.
 */
public abstract class CachedPlaceholder extends Placeholder {
    @PlayerCleaner
    private HashMap<UUID, String> cache = new HashMap<>();

    void updateCache(){
        this.cache.clear();
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            updateCache(player);
        }
    }

    void updateCache(Player player){
        if(player != null) {
            String str = getValue(player);
            if(str != null) {
                this.cache.put(player.getUniqueId(), str);
            } else {
                this.cache.put(player.getUniqueId(), "");
            }
        }
    }

    String getCache(Player player){
        return cache.get(player.getUniqueId());
    }
}
