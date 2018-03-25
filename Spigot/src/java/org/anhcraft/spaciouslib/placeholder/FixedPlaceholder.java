package org.anhcraft.spaciouslib.placeholder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public abstract class FixedPlaceholder extends Placeholder {
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
