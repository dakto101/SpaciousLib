package org.anhcraft.spaciouslib.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultUtils {
    private static Economy eco;

    public static boolean init(){
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }


    public static boolean isInitialized(){
        return eco != null;
    }

    /**
     * Checks a player is having enough money
     * @param player a player
     * @param amount the amount
     * @return true if yes
     */
    public static boolean enough(OfflinePlayer player, double amount) throws Exception {
        if(eco == null){
            throw new Exception();
        }
        double a = eco.getBalance(player);
        double b = a - amount;
        return !(b < 0);
    }

    /**
     * Withdraws a specific amount from the balance of a player
     * @param player a player
     * @param amount the amount
     * @return true if success
     */
    public static boolean withdraw(OfflinePlayer player, double amount) throws Exception {
        if(eco == null){
            throw new Exception();
        }
        double a = eco.getBalance(player);
        double b = a - amount;
        return !(b < 0) && eco.withdrawPlayer(player, amount).transactionSuccess();
    }

    /**
     * Deposits a specific amount into the balance of a player
     * @param player a player
     * @param amount the amount
     * @return true if success
     */
    public static boolean deposit(OfflinePlayer player, double amount) throws Exception {
        if(eco == null){
            throw new Exception();
        }
        return eco.depositPlayer(player, amount).transactionSuccess();
    }

    /**
     * Gets the balance of a player
     * @param player a player
     * @return the balance
     */
    public static double getBalance(OfflinePlayer player) throws Exception {
        if(eco == null){
            throw new Exception();
        }
        return eco.getBalance(player);
    }
}
