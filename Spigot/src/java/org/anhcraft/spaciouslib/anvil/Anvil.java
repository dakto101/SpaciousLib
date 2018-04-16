package org.anhcraft.spaciouslib.anvil;

import org.anhcraft.spaciouslib.listeners.AnvilListener;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an anvil implementation.
 */
public class Anvil {
    public enum Slot {
        /**
         * The item on the left side, represents for the first input of an anvil
         */
        INPUT_LEFT(0),
        /**
         * The item on the center side, represents for the second input of an anvil
         */
        INPUT_RIGHT(1),
        /**
         * The item on the right side, represents for the output of an anvil
         */
        OUTPUT(2);

        private int slot;

        Slot(int slot) {
            this.slot = slot;
        }

        public int getID() {
            return this.slot;
        }
    }

    public abstract static class Handler {
        /**
         * This method will be called if a player clicks on an item of a specific anvil
         * @param player the player
         * @param input the input, which is also the name of the first input item
         * @param item the clicked item
         * @param slot the slot which the clicked item was put on
         */
        public abstract void result(Player player, String input, ItemStack item, Anvil.Slot slot);
    }

    private AnvilWrapper wrapper;
    private Handler handler;
    private Player player;

    /**
     * Creates a new Anvil instance
     * @param player a player who you want to show the anvil for
     * @param handler an anvil handler
     */
    public Anvil(Player player, Handler handler){
        try {
            this.wrapper = (AnvilWrapper) ReflectionUtils.getConstructor(Class.forName("org.anhcraft.spaciouslib.anvil.Anvil_" + GameVersion.getVersion().toString().replace("v", "")), new Group<>(
                    new Class<?>[]{Player.class},
                    new Object[]{player}
            ));
            this.player = player;
            this.handler = handler;
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens this anvil
     * @return this object
     */
    public Anvil open() {
        this.wrapper.open();
        AnvilListener.data.put(this.player, new Group<>(this.wrapper.inv, this.handler));
        return this;
    }

    /**
     * Puts the given item into a specific slot of this anvil
     * @param slot the slot which the item will put on
     * @param item the item
     * @return this object
     */
    public Anvil setItem(Slot slot, ItemStack item) {
        this.wrapper.setItem(slot, item);
        return this;
    }
}
