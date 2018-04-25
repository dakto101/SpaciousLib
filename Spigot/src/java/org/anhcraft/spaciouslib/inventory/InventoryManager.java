package org.anhcraft.spaciouslib.inventory;

import org.anhcraft.spaciouslib.listeners.ClickableItemListener;
import org.anhcraft.spaciouslib.utils.Chat;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A class helps you to manage inventories
 */
public class InventoryManager extends ClickableItemListener {
    private Inventory inv;

    /**
     * Creates a new InventoryManager instance
     * @param size the size of the inventory
     * @param name the inventory name
     * */
    public InventoryManager(String name, int size){
        inv = Bukkit.getServer().createInventory(null, size, Chat.color(name));
    }

    /**
     * Creates a new InventoryManager instance
     * @param inv a Bukkit inventory
     */
    public InventoryManager(Inventory inv){
        this.inv = inv;
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
     * @param index numerical order of the item
     * @param item the item
     * @param run an handler
     * @return this object
     */
    public InventoryManager set(int index, ItemStack item, ClickableItemHandler run){
        this.inv.setItem(index, item);
        a(inv, item, run);
        return this;
    }

    /**
     * Removes an item out of this inventory
     * @param item the item
     * @return this object
     */
    public InventoryManager remove(ItemStack item){
        this.inv.remove(item);
        return this;
    }

    /**
     * Gets an item in this inventory
     * @param column numerical order of column of the item
     * @param row numerical order of row of the item
     * @return the item
     */
    public ItemStack get(int column, int row){
        return this.inv.getItem(column*row);
    }

    /**
     * Gets an item in this inventory
     * @param index the index of the item
     * @return the item
     */
    public ItemStack get(int index){
        return this.inv.getItem(index);
    }

    /**
     * Adds an item into this inventory
     * @param item the item
     * @return this object
     */
    public InventoryManager addItem(ItemStack item) {
        this.inv.addItem(item);
        return this;
    }

    /**
     * Adds a clickable item into this inventory
     * @param item the item
     * @param run an handler
     * @return this object
     */
    public InventoryManager addItem(ItemStack item, ClickableItemHandler run) {
        this.inv.addItem(item);
        a(inv, item, run);
        return this;
    }

    /**
     * Adds an item into the inventory, only when it doesn't exist
     * @param item the item
     * @return this object
     */
    public InventoryManager addUniqueItem(ItemStack item) {
        boolean has = false;
        for(ItemStack i : this.inv){
            if(InventoryUtils.compare(i, item)){
                has = true;
                break;
            }
        }
        if(!has){
            addItem(item);
        }
        return this;
    }

    /**
     * Adds a clickable item into the inventory, only when it doesn't exist
     * @param item the item
     * @param run an handler
     * @return this object
     */
    public InventoryManager addUniqueItem(ItemStack item, ClickableItemHandler run) {
        boolean has = false;
        for(ItemStack i : this.inv){
            if(InventoryUtils.compare(i, item)){
                has = true;
                break;
            }
        }
        if(!has){
            addItem(item);
            a(inv, item, run);
        }
        return this;
    }

    /**
     * Fills all existing slot in this inventory
     * @param item an item
     * @return this object
     */
    public InventoryManager fill(ItemStack item){
        for(int i = 0; i < this.inv.getSize(); i++) {
            int empty = this.inv.firstEmpty();
            if(empty != -1) {
                this.inv.setItem(i, item);
            }
        }
        return this;
    }

    /**
     * Fills all existing slot in this inventory
     * @param item an item
     * @param run an handler
     * @return this object
     */
    public InventoryManager fill(ItemStack item, ClickableItemHandler run){
        for(int i = 0; i < this.inv.getSize(); i++) {
            int empty = this.inv.firstEmpty();
            if(empty != -1) {
                this.inv.setItem(i, item);
                a(inv, item, run);
            }
        }
        return this;
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
     * @param uuid the UUID of the player
     * @return this object
     */
    public InventoryManager open(UUID uuid){
        Bukkit.getServer().getPlayer(uuid).openInventory(this.inv);
        return this;
    }

    /**
     * Opens this inventory for the given player
     * @param player Player object
     * @return this object
     */
    public InventoryManager open(Player player){
        player.openInventory(this.inv);
        return this;
    }

    /**
     * Gets this inventory as a Bukkit Inventory
     * @return a Bukkit Inventory
     */
    public Inventory getInventory(){
        return inv;
    }

    /**
     * Applies this inventory as the inventory of a player
     * @param player the player
     * @return this object
     */
    public InventoryManager apply(Player player){
        player.getInventory().setContents(this.inv.getContents());
        player.updateInventory();
        return this;
    }

    /**
     * Gets all non-null items in this inventory
     * @return list of items
     */
    public List<ItemStack> getItems(){
        List<ItemStack> items = new ArrayList<>();
        for(ItemStack i : inv.getContents()){
            if(!InventoryUtils.isNull(i)){
                items.add(i);
            }
        }
        return items;
    }
}
