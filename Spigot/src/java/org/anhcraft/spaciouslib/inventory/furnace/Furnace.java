package org.anhcraft.spaciouslib.inventory.furnace;

import org.anhcraft.spaciouslib.listeners.FurnaceListener;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a furnace implementation.
 */
public class Furnace extends FurnaceWrapper<Furnace> {
    private FurnaceWrapper wrapper;
    private FurnaceHandler handler;
    private Player player;

    /**
     * Creates a new Furnace instance
     * @param player a player
     * @param handler the handler for this furnace
     */
    public Furnace(Player player, FurnaceHandler handler){
        try {
            this.wrapper = (FurnaceWrapper) ReflectionUtils.getConstructor(Class.forName("org.anhcraft.spaciouslib.inventory.furnace.Furnace_" + GameVersion.getVersion().toString().replace("v", "")), new Group<>(
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
     * Opens this furnace
     * @return this object
     */
    public Furnace open() {
        this.wrapper.open();
        FurnaceListener.data.put(this.player.getUniqueId(), new Group<>(this.wrapper.inv, this.handler));
        return this;
    }

    /**
     * Puts the given item into a specific slot of this furnace
     * @param slot the slot which the item will put on
     * @param item the item
     * @return this object
     */
    public Furnace setItem(FurnaceSlot slot, ItemStack item) {
        this.wrapper.setItem(slot, item);
        return this;
    }

    /**
     * Sets the current burning progress.<br>
     * The maximum value can be set is 200.
     * @param current an integer must below or equals 200. Ex: 100 represents for 50%
     * @return this object
     */
    @Override
    public Furnace setProgress(int current) {
        if(current > 200 || current < 0){
            current = 200;
        }
        this.wrapper.setProgress(current);
        return null;
    }

    /**
     * Sets the current fuel-burning time.
     * @param current the current time
     * @param max the maximum time
     * @return this object
     */
    @Override
    public Furnace setFuelBurnTime(int current, int max) {
        if(current < 0 || max < 0 || current > max){
            current = 0;
        }
        this.wrapper.setFuelBurnTime(current, max);
        return null;
    }
}
