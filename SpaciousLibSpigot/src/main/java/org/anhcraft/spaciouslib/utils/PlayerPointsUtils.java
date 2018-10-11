package org.anhcraft.spaciouslib.utils;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.OfflinePlayer;

public class PlayerPointsUtils {
    public static PlayerPointsAPI api;

    public static void init(){
        api = PlayerPoints.getPlugin(PlayerPoints.class).getAPI();
    }

    public static boolean isInitialized(){
        return api != null;
    }

    /**
     * Reset money from a player
     * @param player a player
     * @return true if yes
     */
    public static boolean reset(OfflinePlayer player) {
        if(api == null){
            return false;
        }
        return api.reset(player.getUniqueId());
    }

    /**
     * Checks a player is having enough point
     * @param player player
     * @param amount amount
     * @return true if yes
     */
    public static boolean enough(OfflinePlayer player, int amount) {
        if(api == null){
            return false;
        }
        int a = api.look(player.getUniqueId());
        int b = a - amount;
        return !(b < 0);
    }

    /**
     * Withdraws a specific amount from the balance of a player
     * @param player player
     * @param amount amount
     * @return true if success
     */
    public static boolean withdraw(OfflinePlayer player, int amount) {
        if(api == null){
            return false;
        }
        int a = api.look(player.getUniqueId());
        int b = a - amount;
        return !(b < 0) && api.take(player.getUniqueId(), amount);
    }

    /**
     * Deposits a specific amount into the balance of a player
     * @param player player
     * @param amount amount
     * @return true if success
     */
    public static boolean deposit(OfflinePlayer player, int amount) {
        if(api == null){
            return false;
        }
        return api.give(player.getUniqueId(), amount);
    }

    /**
     * Gets the balance of a player
     * @param player a player
     * @return the balance
     */
    public static int getBalance(OfflinePlayer player) {
        if(api == null){
            return 0;
        }
        return api.look(player.getUniqueId());
    }

    /**
     * Set a player's balance
     * @param player player
     * @param amount amount
     * @return true if success
     */
    public static boolean set(OfflinePlayer player, int amount) {
        if(api == null){
            return false;
        }
        return api.set(player.getUniqueId(), amount);
    }
}
