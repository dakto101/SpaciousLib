package org.anhcraft.spaciouslib.anvil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;

/**
 * Represents an anvil implementation.<br>
 * AnvilForm is easier to use than Anvil because you don't need to listen to the events of the anvil.
 */
public class AnvilForm {
    public static LinkedHashMap<Player, AnvilInfo> anvils = new LinkedHashMap<>();

    /**
     * Creates a new anvil and opens it for a specific player
     * @param item an item which is on the left of the anvil
     * @param runnable a runnable triggers when a player clicks on anvil's right item
     * @param player the player
     */
    public static boolean create(ItemStack item, AnvilRunnable runnable, Player player, JavaPlugin plugin){
        if(anvils.containsKey(player)){
            return false;
        }
        String id = player.getName()+System.currentTimeMillis()+plugin.getName();
        Anvil a = new Anvil(player, id);
        a.setSlot(AnvilSlot.INPUT_LEFT, item);
        a.open();
        anvils.put(player, new AnvilInfo() {
            @Override
            public AnvilRunnable run() {
                return runnable;
            }

            @Override
            public String id() {
                return id;
            }
        });
        return true;
    }

    public static abstract class AnvilRunnable {
        /**
         * This method will be called if a player clicks on the right item of the anvil
         * @param input the text which the player types
         */
        public abstract void run(String input);
    }

    public static abstract class AnvilInfo{
        public abstract AnvilRunnable run();
        public abstract String id();
    }
}
