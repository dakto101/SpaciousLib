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
    public ClickableItemHandler[] clickableSlots;

    /**
     * Creates a new InventoryManager instance
     * @param size the size of the inventory
     * @param name the inventory name
     * */
    public InventoryManager(String name, int size){
        if(size % 9 != 0){
            size = size + (9 - size % 9);
        }
        inv = Bukkit.getServer().createInventory(null, size, Chat.color(name));
        clickableSlots = new ClickableItemHandler[size];
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
        clickableSlots[index] = run;
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
     * Removes an existed item
     * @param index the index of the item
     * @return this object
     */
    public InventoryManager remove(int index){
        this.inv.setItem(index, null);
        clickableSlots[index] = null;
        return this;
    }

    /**
     * Removes any existed item which are equal with the given item
     * @param item the item which will be removed
     * @return this object
     */
    public InventoryManager remove(ItemStack item){
        for(int i = 0; i < this.inv.getSize(); i++) {
            if(InventoryUtils.compare(item, get(i))){
                remove(i);
            }
        }
        return this;
    }

    /**
     * Adds an item into this inventory
     * @param item the item
     * @return this object
     */
    public InventoryManager addItem(ItemStack item) {
        int emptySlot = this.inv.firstEmpty();
        if(emptySlot != -1){
            this.inv.setItem(emptySlot, item);
        }
        return this;
    }

    /**
     * Adds a clickable item into this inventory
     * @param item the item
     * @param run an handler
     * @return this object
     */
    public InventoryManager addItem(ItemStack item, ClickableItemHandler run) {
        int emptySlot = this.inv.firstEmpty();
        if(emptySlot != -1){
            this.inv.setItem(emptySlot, item);
            clickableSlots[emptySlot] = run;
        }
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
            addItem(item, run);
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
                set(i, item);
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
                set(i, item, run);
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
        clickableSlots = new ClickableItemHandler[inv.getSize()];
        return this;
    }

    /**
     * Opens this inventory for the given player
     * @deprecated
     * @param name the player name
     * @return this object
     */
    public InventoryManager open(String name){
        for(int i = 0; i < clickableSlots.length; i++){
            ClickableItemHandler handler = clickableSlots[i];
            if(handler != null) {
                a(this.inv, i, handler);
            }
        }
        Bukkit.getServer().getPlayer(name).openInventory(this.inv);
        return this;
    }

    /**
     * Opens this inventory for the given player
     * @param uuid the UUID of the player
     * @return this object
     */
    public InventoryManager open(UUID uuid){
        for(int i = 0; i < clickableSlots.length; i++){
            ClickableItemHandler handler = clickableSlots[i];
            if(handler != null) {
                a(this.inv, i, handler);
            }
        }
        Bukkit.getServer().getPlayer(uuid).openInventory(this.inv);
        return this;
    }

    /**
     * Opens this inventory for the given player
     * @param player Player object
     * @return this object
     */
    public InventoryManager open(Player player){
        for(int i = 0; i < clickableSlots.length; i++){
            ClickableItemHandler handler = clickableSlots[i];
            if(handler != null) {
                a(this.inv, i, handler);
            }
        }
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
     * Applies this inventory for the given player
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
     * @return list of item stacks
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
