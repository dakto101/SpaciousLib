package org.anhcraft.spaciouslib.utils;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultUtils {
    public static Economy eco;
    public static Permission perm;
    public static Chat chat;

    public static void init(){
        RegisteredServiceProvider<Economy> rsp1 = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp1 != null) {
            eco = rsp1.getProvider();
        }
        RegisteredServiceProvider<Permission> rsp2 = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp2 != null) {
            perm = rsp2.getProvider();
        }
        RegisteredServiceProvider<Chat> rsp3 = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp3 != null) {
            chat = rsp3.getProvider();
        }
    }

    public static boolean isInitialized(){
        return eco != null || perm != null || chat != null;
    }

    /**
     * Checks a player is having enough money
     * @param player a player
     * @param amount the amount
     * @return true if yes
     */
    public static boolean enough(OfflinePlayer player, double amount) {
        if(eco == null || !eco.isEnabled()){
            return false;
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
    public static boolean withdraw(OfflinePlayer player, double amount) {
        if(eco == null || !eco.isEnabled()){
            return false;
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
    public static boolean deposit(OfflinePlayer player, double amount) {
        if(eco == null || !eco.isEnabled()){
            return false;
        }
        return eco.depositPlayer(player, amount).transactionSuccess();
    }

    /**
     * Gets the balance of a player
     * @param player a player
     * @return the balance
     */
    public static double getBalance(OfflinePlayer player) {
        if(eco == null || !eco.isEnabled()){
            return 0;
        }
        return eco.getBalance(player);
    }

    /**
     * Gets the primary permission group which a player is in
     * @param player a player
     * @return a permission group
     */
    public static String getPrimaryPermissionGroup(Player player) {
        if(perm == null || !perm.isEnabled() || !perm.hasGroupSupport()){
            return null;
        }
        return perm.getPrimaryGroup(player);
    }

    /**
     * Gets all permission groups which a player is in
     * @param player a player
     * @return an array of permission groups
     */
    public static String[] getPlayerPermissionGroups(Player player) {
        if(perm == null || !perm.isEnabled() || !perm.hasGroupSupport()){
            return null;
        }
        return perm.getPlayerGroups(player);
    }

    /**
     * Gets all permission groups
     * @return an array of permission groups
     */
    public static String[] getPermissionGroups() {
        if(perm == null || !perm.isEnabled() || !perm.hasGroupSupport()){
            return null;
        }
        return perm.getGroups();
    }

    /**
     * Gets all chat groups
     * @return an array of chat groups
     */
    public static String[] getChatGroups() {
        if(perm == null || !chat.isEnabled()){
            return null;
        }
        return chat.getGroups();
    }

    /**
     * Gets all chat groups which a player is in
     * @param player a player
     * @return an array of chat groups
     */
    public static String[] getPlayerChatGroups(Player player) {
        if(chat == null || !chat.isEnabled()){
            return null;
        }
        return chat.getPlayerGroups(player);
    }
}
