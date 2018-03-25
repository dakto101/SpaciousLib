package org.anhcraft.spaciouslib.inventory;

import org.anhcraft.spaciouslib.listeners.InteractItemListener;
import org.anhcraft.spaciouslib.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryManager extends InteractItemListener {
    private Inventory inv;

    /**
     * Creates a new inventory
     *
     * @param size the size of inventory
     * @param name the inventory name
     *
     * */
    public InventoryManager(String name, int size){
        inv = Bukkit.getServer().createInventory(null, size, Strings.color(name));
    }

    /**
     * Creates a new inventory
     *
     * @param inv Bukkit inventory
     *
     */
    public InventoryManager(Inventory inv){
        this.inv = inv;
    }

    /**
     * Stores an item in that inventory
     * @param column numerical order of column
     * @param row    numerical order of row
     * @param item   the item
     */
    public InventoryManager set(int column, int row, ItemStack item){
        this.inv.setItem(column*row, item);
        return this;
    }

    /**
     * Stores an item in that inventory
     * @param index numerical order
     * @param item   the item
     */
    public InventoryManager set(int index, ItemStack item){
        this.inv.setItem(index, item);
        return this;
    }

    /**
     * Stores an clickable item in that inventory
     * @param column numerical order of column
     * @param row    numerical order of row
     * @param item   the item
     * @param run    interact event
     */
    public InventoryManager set(int column, int row, ItemStack item, InteractItemRunnable run){
        this.inv.setItem(column*row, item);
        a(inv, item, run);
        return this;
    }

    /**
     * Stores an clickable item in that inventory
     * @param index numerical order
     * @param item   the item
     * @param run    interact event
     */
    public InventoryManager set(int index, ItemStack item, InteractItemRunnable run){
        this.inv.setItem(index, item);
        a(inv, item, run);
        return this;
    }

    /**
     * Removes an item out of that inventory
     *
     * @param item this item
     */
    public InventoryManager remove(ItemStack item){
        this.inv.remove(item);
        return this;
    }

    /**
     * Gets an item in that inventory
     *
     * @param column numerical order of column
     * @param row    numerical order of row
     *
     * @return the item stack
     */
    public ItemStack get(int column, int row){
        return this.inv.getItem(column*row);
    }

    /**
     * Gets an item in that inventory
     *
     * @param index index
     *
     * @return the item stack
     */
    public ItemStack get(int index){
        return this.inv.getItem(index);
    }

    /**
     * Clears out the whole inventory
     */
    public InventoryManager clear(){
        this.inv.clear();
        return this;
    }

    /**
     * Opens an inventory for a player
     *
     * @deprecated
     * @param name the player name
     */
    @Deprecated
    public InventoryManager open(String name){
        Bukkit.getServer().getPlayer(name).openInventory(this.inv);
        return this;
    }

    /**
     * Opens an inventory for a player
     *
     * @param uuid the uuid
     */
    public InventoryManager open(UUID uuid){
        Bukkit.getServer().getPlayer(uuid).openInventory(this.inv);
        return this;
    }

    /**
     * Opens an inventory for a player
     *
     * @param player the player
     */
    public InventoryManager open(Player player){
        player.openInventory(this.inv);
        return this;
    }

    /**
     * Gets inventory
     *
     * @return inventory
     */
    public Inventory getInventory(){
        return inv;
    }

    /**
     * Gets all item which isn't null in that inventory
     *
     * @return all items
     */
    public List<ItemStack> getAllItems(){
        List<ItemStack> items = new ArrayList<>();
        for(ItemStack i : inv.getContents()){
            if(i != null && !i.getType().equals(Material.AIR)){
                items.add(i);
            }
        }
        return items;
    }
}
