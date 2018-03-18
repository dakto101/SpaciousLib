package org.anhcraft.spaciouslib.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.LinkedHashMap;

public class CooldownUtils {
    private static LinkedHashMap<Player, LinkedHashMap<String, Long>> data = new LinkedHashMap<>();

    private Plugin plugin;

    public CooldownUtils(Plugin plugin){
        this.plugin = plugin;
    }

    public void setCooldown(String name, Player p){
        name = this.plugin.getName()+"#"+name;
        LinkedHashMap<String, Long> cooldownData = new LinkedHashMap<>();
        if(data.containsKey(p)){
            cooldownData = data.get(p);
        }
        cooldownData.put(name, System.currentTimeMillis());
        data.put(p, cooldownData);
    }

    public boolean isTimeout(String name, Player p, int seconds){
        name = this.plugin.getName()+"#"+name;
        if(data.containsKey(p)){
            LinkedHashMap<String, Long> cooldownData = data.get(p);
            if(cooldownData.containsKey(name)){
                if((System.currentTimeMillis()-cooldownData.get(name)) < (seconds*1000)){
                    return false;
                }
            }
        }
        return true;
    }

    public int timeLeft(String name, Player p, int i){
        name = this.plugin.getName()+"#"+name;
        if(data.containsKey(p)){
            LinkedHashMap<String, Long> cooldownData = data.get(p);
            if(cooldownData.containsKey(name)){
                return i - ((int) ((System.currentTimeMillis()-cooldownData.get(name))/1000));
            }
        }
        return 0;
    }
}
