package org.anhcraft.spaciouslib.inventory;

import org.anhcraft.spaciouslib.attribute.Attribute;
import org.anhcraft.spaciouslib.attribute.AttributeModifier;
import org.anhcraft.spaciouslib.nbt.NBTCompound;
import org.anhcraft.spaciouslib.nbt.NBTLoader;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.Chat;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * This class helps you to manage the stack of items
 */
public class ItemManager {
    protected ItemStack item;
    private NBTCompound nbt;

    /**
     * Creates ItemManager instance
     *
     * @param item ItemStack object
     */
    public ItemManager(ItemStack item) {
        this.item = item;
        nbt = NBTLoader.fromItem(item);
    }

    /**
     * Creates ItemManager instance
     *
     * @param type the type of the item
     * @param amount the amount of stack
     */
    public ItemManager(Material type, int amount) {
        this.item = new ItemStack(type, amount);
        nbt = NBTLoader.fromItem(item);
    }

    /**
     * Creates ItemManager instance
     *
     * @param name the name of the item
     * @param type the type of the item
     * @param amount the amount of stack
     */
    public ItemManager(String name, Material type, int amount) {
        this.item = new ItemStack(type, amount);
        setName(name);
        nbt = NBTLoader.fromItem(item);
    }

    /**
     * Creates ItemManager instance
     *
     * @param name the name of the item
     * @param type the type of the item
     * @param amount the amount of stack
     * @param durability the durability of the item
     */
    public ItemManager(String name, Material type, int amount, short durability) {
        this.item = new ItemStack(type, amount);
        setName(name);
        setDurability(durability);
        nbt = NBTLoader.fromItem(item);
    }

    /**
     * Gets the name of this item stack
     * @return the name of this item stack
     */
    public String getName() {
        ItemMeta a = this.item.getItemMeta();
        return a.getDisplayName();
    }

    /**
     * Sets a new name for this item stack
     * @param name new name for this item stack
     * @return this object
     */
    public ItemManager setName(String name) {
        ItemMeta a = this.item.getItemMeta();
        a.setDisplayName(Chat.color(name));
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Adds an enchantment into this item stack
     * @param enchant Enchantment object
     * @param level the level of the enchantment
     * @return this object
     */
    public ItemManager addEnchant(Enchantment enchant, int level) {
        ItemMeta a = this.item.getItemMeta();
        a.addEnchant(enchant, level, true);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Removes an enchantment out of this item stack
     * @param enchant Enchantment object
     * @return this object
     */
    public ItemManager removeEnchant(Enchantment enchant) {
        ItemMeta a = this.item.getItemMeta();
        a.removeEnchant(enchant);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Gets all enchantments of this item stack
     * @return a map of enchantments with levels
     */
    public Map<Enchantment, Integer> getEnchants() {
        ItemMeta a = this.item.getItemMeta();
        return a.getEnchants();
    }

    /**
     * Gets the level of the given enchantment of this item stack
     *
     * @param enchant Enchantment object
     * @return the level of that enchantment
     */
    public int getEnchantLevel(Enchantment enchant) {
        ItemMeta a = this.item.getItemMeta();
        return a.getEnchantLevel(enchant);
    }

    /**
     * Adds a new lore into this item stack
     *
     * @param text the lore
     * @return this object
     */
    public ItemManager addLore(String text) {
        ItemMeta a = this.item.getItemMeta();
        List<String> lores;
        if(a.hasLore()) {
            lores = a.getLore();
        } else {
            lores = new ArrayList<>();
        }
        lores.add(Chat.color(text));
        a.setLore(lores);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Adds new lores into this item stack
     *
     * @param texts list of lores
     * @return this object
     */
    public ItemManager addLores(List<String> texts) {
        ItemMeta a = this.item.getItemMeta();
        List<String> lores;
        if(a.hasLore()) {
            lores = a.getLore();
        } else {
            lores = new ArrayList<>();
        }
        for(String b : texts) {
            lores.add(Chat.color(b));
        }
        a.setLore(lores);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Adds new lores into this item stack
     *
     * @param texts array of lores
     * @return this object
     */
    public ItemManager addLores(String... texts) {
        ItemMeta a = this.item.getItemMeta();
        List<String> lores;
        if(a.hasLore()) {
            lores = a.getLore();
        } else {
            lores = new ArrayList<>();
        }
        for(String b : texts) {
            lores.add(Chat.color(b));
        }
        a.setLore(lores);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Sets the lores of this item stack
     *
     * @param texts list of lores
     * @return this object
     */
    public ItemManager setLores(List<String> texts) {
        ItemMeta a = this.item.getItemMeta();
        List<String> lores = new ArrayList<>();
        for(String b : texts) {
            lores.add(Chat.color(b));
        }
        a.setLore(lores);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Sets the lores of this item stack
     *
     * @param texts array of lores
     * @return this object
     */
    public ItemManager setLores(String... texts) {
        ItemMeta a = this.item.getItemMeta();
        List<String> lores = new ArrayList<>();
        for(String b : texts) {
            lores.add(Chat.color(b));
        }
        a.setLore(lores);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Removes a specific lore out of this item stack
     *
     * @param index the index of the lore
     * @return this object
     */
    public ItemManager removeLore(int index) {
        ItemMeta a = this.item.getItemMeta();
        List<String> lores = a.getLore();
        lores.remove(index);
        a.setLore(lores);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Gets all lores of this item stack
     * @return list of lores
     */
    public List<String> getLores() {
        ItemMeta a = this.item.getItemMeta();
        return a.getLore();
    }

    /**
     * Adds a flag into this item stack
     * @param flag the type of flag
     * @return this object
     */
    public ItemManager addFlag(ItemFlag flag) {
        ItemMeta a = this.item.getItemMeta();
        a.addItemFlags(flag);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Removes a flag out of this item stack
     * @param flag the type of flag
     * @return this object
     */
    public ItemManager removeFlag(ItemFlag flag) {
        ItemMeta a = this.item.getItemMeta();
        a.removeItemFlags(flag);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Checks does this item stack has the given flag
     * @param flag the type of flag
     * @return true if it has
     */
    public Boolean hasFlag(ItemFlag flag) {
        ItemMeta a = this.item.getItemMeta();
        return a.hasItemFlag(flag);
    }

    /**
     * Gets all flags of this item stack
     * @return a collection of flags
     */
    public Set<ItemFlag> getFlags() {
        ItemMeta a = this.item.getItemMeta();
        return a.getItemFlags();
    }

    /**
     * Sets a new durability value for this item stack
     * @param durability new durability value
     * @return this object
     */
    public ItemManager setDurability(short durability) {
        this.item.setDurability(durability);
        return this;
    }

    /**
     * Gets the durability value of this item stack
     * @return the durability value
     */
    public short getDurability() {
        return this.item.getDurability();
    }

    /**
     * Sets new type for this item stack
     * @param type new type
     * @return this object
     */
    public ItemManager setType(Material type) {
        this.item.setType(type);
        return this;
    }

    /**
     * Gets the type of this item
     * @return the type of this item
     */
    public Material getType() {
        return this.item.getType();
    }

    /**
     * Sets the amount value for this item stack
     * @param amount the amount of this item stack
     * @return this object
     */
    public ItemManager setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    /**
     * Gets amount value of this item stack
     * @return the amount value
     */
    public int getAmount() {
        return this.item.getAmount();
    }

    /**
     * Gets this item stack as an ItemStack object
     * @return ItemStack object
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Makes this item stack unbreakable or not
     * @param unbreakable true if you want to make this item stack unbreakable
     * @return this object
     */
    public ItemManager setUnbreakable(Boolean unbreakable) {
        if(unbreakable) {
            item = nbt.setBoolean("Unbreakable", true).toItem(item);
        } else {
            item = nbt.remove("Unbreakable").toItem(item);
        }
        return this;
    }

    /**
     * Checks is this item stack unbreakable
     *
     * @return true if yes
     */
    public Boolean isUnbreakable() {
        return nbt.hasKey("Unbreakable");
    }

    /**
     * Adds an attribute modifier into this item stack
     * @param type the type of the attribute
     * @param modifier AttributeModifier object
     * @param slot equipment slot
     * @return this object
     */
    public ItemManager addAttributeModifier(Attribute.Type type, AttributeModifier modifier, EquipSlot slot) {
        List<NBTCompound> l = nbt.getList("AttributeModifiers");
        if(l == null){
            l = new ArrayList<>();
        }
        NBTCompound c = NBTLoader.create()
                .set("AttributeName", type.getId())
                .set("Name", modifier.getName())
                .set("Amount", modifier.getAmount())
                .set("Operation", modifier.getOperation().getId())
                .set("UUIDLeast", modifier.getUniqueId().getLeastSignificantBits())
                .set("UUIDMost", modifier.getUniqueId().getMostSignificantBits())
                .set("Slot", slot.toString().toLowerCase());
        l.add(c);
        item = nbt.setList("AttributeModifiers", l).toItem(item);
        return this;
    }

    /**
     * Removes all attribute modifiers which has specified name out of this item stack
     * @param name the name of attribute modifiers
     * @return this object
     */
    public ItemManager removeAttributeModifiers(String name) {
        List<NBTCompound> l = nbt.getList("AttributeModifiers");
        if(l == null){
            return this;
        }
        List<NBTCompound> nl = new ArrayList<>();
        for(NBTCompound m : l){
            if(!m.getString("Name").equals(name)){
                nl.add(m);
            }
        }
        item = nbt.setList("AttributeModifiers", nl).toItem(item);
        return this;
    }

    /**
     * Removes all attribute modifiers which has specified type out of this item stack
     * @param type the type of attribute modifiers
     * @return this object
     */
    public ItemManager removeAttributeModifiers(Attribute.Type type) {
        List<NBTCompound> l = nbt.getList("AttributeModifiers");
        if(l == null){
            return this;
        }
        List<NBTCompound> nl = new ArrayList<>();
        for(NBTCompound m : l){
            if(!m.getString("AttributeName").equals(type.getId().toLowerCase())){
                nl.add(m);
            }
        }
        item = nbt.setList("AttributeModifiers", nl).toItem(item);
        return this;
    }

    /**
     * Removes all attribute modifiers out of this item stack
     * @return this object
     */
    public ItemManager removeAttributeModifiers() {
        item = nbt.setList("AttributeModifiers", new ArrayList<NBTCompound>()).toItem(item);
        return this;
    }

    /**
     * Removes all attribute modifiers which has specified slot equipment out of this item stack
     * @param slot slot equipment
     * @return this object
     */
    public ItemManager removeAttributeModifiers(EquipSlot slot) {
        List<NBTCompound> l = nbt.getList("AttributeModifiers");
        if(l == null){
            return this;
        }
        List<NBTCompound> nl = new ArrayList<>();
        for(NBTCompound m : l){
            if(!m.getString("Slot").equals(slot.toString().toLowerCase())){
                nl.add(m);
            }
        }
        item = nbt.setList("AttributeModifiers", nl).toItem(item);
        return this;
    }

    /**
     * Gets all attribute modifiers of this item
     * @return all attribute modifiers
     */
    public LinkedHashMap<AttributeModifier,
            Group<EquipSlot, Attribute.Type>> getAttributeModifiers(){
        LinkedHashMap<AttributeModifier,
               Group<EquipSlot, Attribute.Type>> modifiers = new LinkedHashMap<>();
        List<NBTCompound> l = nbt.getList("AttributeModifiers");
        if(l == null){
            return modifiers;
        }
        for(NBTCompound modifier : l){
            modifiers.put(new AttributeModifier(new UUID(modifier.getLong("UUIDMost"), modifier.getLong("UUIDLeast")), modifier.getString("Name"), modifier.getDouble("Amount"), AttributeModifier.Operation.getById(modifier.getInt("Operation"))),
                    new Group<>(EquipSlot.valueOf(modifier.getString("Slot").toUpperCase()),
                            Attribute.Type.getById(modifier.getString("AttributeName"))));
        }
        return modifiers;
    }
}
