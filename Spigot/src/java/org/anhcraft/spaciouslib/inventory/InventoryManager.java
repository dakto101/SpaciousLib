package org.anhcraft.spaciouslib.inventory;

import org.anhcraft.spaciouslib.listeners.InteractItemListener;
import org.anhcraft.spaciouslib.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A class helps you to manage inventories
 */
public class InventoryManager extends InteractItemListener {
    private Inventory inv;

    /**
     * Creates a new InventoryManager instance
     *
     * @param size the size of the inventory
     * @param name the inventory name
     *
     * */
    public InventoryManager(String name, int size){
        inv = Bukkit.getServer().createInventory(null, size, Chat.color(name));
    }

    /**
     * Creates a new InventoryManager instance
     *
     * @param inv a Bukkit inventory
     *
     */
    public InventoryManager(Inventory inv){
        this.inv = inv;
    }

    /**
     * Stores the given item in this inventory
     * @param column numerical order of column of the item
     * @param row numerical order of row of the item
     * @param item the item
     * @return this object
     */
    public InventoryManager set(int column, int row, ItemStack item){
        this.inv.setItem(column*row, item);
        return this;
    }

    /**
     * Stores the given item in this inventory
     * @param index numerical order of the item
     * @param item the item
     * @return this object
     */
    public InventoryManager set(int index, ItemStack item){
        this.inv.setItem(index, item);
        return this;
    }

    /**
     * Stores the given clickable item in that inventory
     * @param column numerical order of column of the item
     * @param row numerical order of row of the item
     * @param item the item
     * @param run InteractItemRunnable object
     * @return this object
     */
    public InventoryManager set(int column, int row, ItemStack item, InteractItemRunnable run){
        this.inv.setItem(column*row, item);
        a(inv, item, run);
        return this;
    }

    /**
     * Stores the given clickable item in that inventory
     * @param index numerical order of the item
     * @param item the item
     * @param run InteractItemRunnable object
     * @return this object
     */
    public InventoryManager set(int index, ItemStack item, InteractItemRunnable run){
        this.inv.setItem(index, item);
        a(inv, item, run);
        return this;
    }

    /**
     * Removes an item out of this inventory
     *
     * @param item the item
     * @return this object
     */
    public InventoryManager remove(ItemStack item){
        this.inv.remove(item);
        return this;
    }

    /**
     * Gets an item in this inventory
     *
     * @param column numerical order of column of the item
     * @param row numerical order of row of the item
     *
     * @return the item
     */
    public ItemStack get(int column, int row){
        return this.inv.getItem(column*row);
    }

    /**
     * Gets an item in this inventory
     *
     * @param index the index of the item
     *
     * @return the item
     */
    public ItemStack get(int index){
        return this.inv.getItem(index);
    }

    /**
     * Clears out the whole inventory
     * @return this object
     */
    public InventoryManager clear(){
        this.inv.clear();
        return this;
    }

    /**
     * Opens this inventory for the given player
     *
     * @deprecated
     * @param name the player name
     * @return this object
     */
    @Deprecated
    public InventoryManager open(String name){
        Bukkit.getServer().getPlayer(name).openInventory(this.inv);
        return this;
    }

    /**
     * Opens this inventory for the given player
     *
     * @param uuid the UUID of the player
     * @return this object
     */
    public InventoryManager open(UUID uuid){
        Bukkit.getServer().getPlayer(uuid).openInventory(this.inv);
        return this;
    }

    /**
     * Opens this inventory for the given player
     *
     * @param player Player object
     * @return this object
     */
    public InventoryManager open(Player player){
        player.openInventory(this.inv);
        return this;
    }

    /**
     * Gets this inventory as a Bukkit Inventory
     *
     * @return a Bukkit Inventory
     */
    public Inventory getInventory(){
        return inv;
    }

    /**
     * Gets all items which aren't null in this inventory
     *
     * @return list of items
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
