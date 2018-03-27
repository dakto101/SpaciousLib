package org.anhcraft.spaciouslib.utils;

import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

/**
 * A class helps you to manage cooldown times<br>
 * One player can have unlimited cooldown times<br>
 * A cooldown time is defined by its name<br>
 * The name of the cooldown time must be unique
 */
public class CooldownUtils {
    private static LinkedHashMap<Player, LinkedHashMap<String, Long>> data = new LinkedHashMap<>();

    /**
     * Initializes the given cooldown time
     * @param name the name of the cooldown time
     * @param p the player
     */
    public void setCooldown(String name, Player p){
        LinkedHashMap<String, Long> cooldownData = new LinkedHashMap<>();
        if(data.containsKey(p)){
            cooldownData = data.get(p);
        }
        cooldownData.put(name, System.currentTimeMillis());
        data.put(p, cooldownData);
    }

    /**
     * Checks do the current time exceed the cooldown time<br>
     * With different cooldown time, there will different result
     * @param name the name of the cooldown time
     * @param p the player
     * @param seconds the duration of the cooldown (in seconds)
     * @return true if the current time exceed the cooldown time
     */
    public boolean isTimeout(String name, Player p, int seconds){
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

    /**
     * Gets the time left until the end of the cooldown time
     * @param name the name of the cooldown time
     * @param p the player
     * @param seconds the duration of the cooldown (in seconds)
     * @return the time left
     */
    public int timeLeft(String name, Player p, int seconds){
        if(data.containsKey(p)){
            LinkedHashMap<String, Long> cooldownData = data.get(p);
            if(cooldownData.containsKey(name)){
                return seconds - ((int) ((System.currentTimeMillis()-cooldownData.get(name))/1000));
            }
        }
        return 0;
    }
}
