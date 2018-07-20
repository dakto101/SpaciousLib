package org.anhcraft.spaciouslib.inventory.anvil;

import org.anhcraft.spaciouslib.listeners.AnvilListener;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an anvil implementation.
 */
public class Anvil extends AnvilWrapper<Anvil> {
    private AnvilWrapper wrapper;
    private AnvilHandler handler;
    private Player player;

    /**
     * Creates a new Anvil instance
     * @param player a player
     * @param handler the handler for this anvil
     */
    public Anvil(Player player, AnvilHandler handler){
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
        AnvilListener.data.put(this.player.getUniqueId(), new Group<>(this.wrapper.inv, this.handler));
        return this;
    }

    /**
     * Puts the given item into a specific slot of this anvil
     * @param slot the slot which the item will put on
     * @param item the item
     * @return this object
     */
    public Anvil setItem(AnvilSlot slot, ItemStack item) {
        this.wrapper.setItem(slot, item);
        return this;
    }
}
