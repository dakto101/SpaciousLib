package org.anhcraft.spaciouslib.utils;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;

import java.util.UUID;

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
     * @param id the unique id of the player
     * @return true if yes
     */
    public static boolean reset(UUID id) {
        if(api == null){
            return false;
        }
        return api.reset(id);
    }

    /**
     * Checks a player is having enough point
     * @param id the unique id of the player
     * @param amount amount
     * @return true if yes
     */
    public static boolean enough(UUID id, int amount) {
        if(api == null){
            return false;
        }
        int a = api.look(id);
        int b = a - amount;
        return !(b < 0);
    }

    /**
     * Withdraws a specific amount from the balance of a player
     * @param id the unique id of the player
     * @param amount amount
     * @return true if success
     */
    public static boolean withdraw(UUID id, int amount) {
        if(api == null){
            return false;
        }
        int a = api.look(id);
        int b = a - amount;
        return !(b < 0) && api.take(id, amount);
    }

    /**
     * Deposits a specific amount into the balance of a player
     * @param id the unique id of the player
     * @param amount amount
     * @return true if success
     */
    public static boolean deposit(UUID id, int amount) {
        if(api == null){
            return false;
        }
        return api.give(id, amount);
    }

    /**
     * Gets the balance of a player
     * @param id the unique id of the player
     * @return the balance
     */
    public static int getBalance(UUID id) {
        if(api == null){
            return 0;
        }
        return api.look(id);
    }

    /**
     * Set a player's balance
     * @param id the unique id of the player
     * @param amount amount
     * @return true if success
     */
    public static boolean set(UUID id, int amount) {
        if(api == null){
            return false;
        }
        return api.set(id, amount);
    }
}
