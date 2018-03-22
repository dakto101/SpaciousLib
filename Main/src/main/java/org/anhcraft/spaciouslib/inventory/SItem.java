package org.anhcraft.spaciouslib.inventory;

import org.anhcraft.spaciouslib.nbt.NBTCompoundWrapper;
import org.anhcraft.spaciouslib.nbt.NBTManager;
import org.anhcraft.spaciouslib.utils.Strings;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class SItem {
    protected ItemStack item;

    /**
     * Imports from an item
     *
     * @param item an item
     */
    public SItem(ItemStack item) {
        this.item = item;
    }

    /**
     * Creates a new item
     *
     * @param name   item's name
     * @param type   item's type
     * @param amount item's amount
     *
     */
    public SItem(String name, Material type, int amount) {
        this.item = new ItemStack(type, amount);
        name(name);
    }

    /**
     * Creates a new item
     *
     * @param name   item's name
     * @param type   item's type
     * @param amount item's amount
     * @param durability item's durability
     *
     */
    public SItem(String name, Material type, int amount, short durability) {
        this.item = new ItemStack(type, amount);
        name(name);
        durability(durability);
    }

    /**
     * Gets name of that item
     */
    public String name() {
        ItemMeta a = this.item.getItemMeta();
        return a.getDisplayName();
    }

    /**
     * Sets a name for that item
     */
    public SItem name(String name) {
        ItemMeta a = this.item.getItemMeta();
        a.setDisplayName(Strings.color(name));
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Adds an enchantment to that item
     * @param enchant enchantment's name
     * @param level   enchantment's level
     */
    public SItem addEnchant(Enchantment enchant, int level) {
        ItemMeta a = this.item.getItemMeta();
        a.addEnchant(enchant, level, true);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Removes an enchantment out of that item
     */
    public SItem removeEnchant(Enchantment enchant) {
        ItemMeta a = this.item.getItemMeta();
        a.removeEnchant(enchant);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Gets all enchantments of that item
     */
    public Map<Enchantment, Integer> enchants() {
        ItemMeta a = this.item.getItemMeta();
        return a.getEnchants();
    }

    /**
     * Gets level of an enchantment of that item
     *
     * @param enchant an enchantment
     */
    public int enchant(Enchantment enchant) {
        ItemMeta a = this.item.getItemMeta();
        return a.getEnchantLevel(enchant);
    }

    /**
     * Adds a new lore line to that item
     *
     * @param text a lore
     */
    public SItem addLore(String text) {
        ItemMeta a = this.item.getItemMeta();
        List<String> lores;
        if(a.hasLore()) {
            lores = a.getLore();
        } else {
            lores = new ArrayList<>();
        }
        lores.add(Strings.color(text));
        a.setLore(lores);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Adds list of lores to that item
     *
     * @param texts list of lores
     */
    public SItem addLores(List<String> texts) {
        ItemMeta a = this.item.getItemMeta();
        List<String> lores;
        if(a.hasLore()) {
            lores = a.getLore();
        } else {
            lores = new ArrayList<>();
        }
        for(String b : texts) {
            lores.add(Strings.color(b));
        }
        a.setLore(lores);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Sets lores of that item
     *
     * @param texts a list of lores
     */
    public SItem setLores(List<String> texts) {
        ItemMeta a = this.item.getItemMeta();
        List<String> lores = new ArrayList<>();
        for(String b : texts) {
            lores.add(Strings.color(b));
        }
        a.setLore(lores);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Removes a specific lore line out of that item
     */
    public SItem removeLore(int index) {
        ItemMeta a = this.item.getItemMeta();
        List<String> lores = a.getLore();
        lores.remove(index);
        a.setLore(lores);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Gets all lores of that item
     */
    public List<String> lore() {
        ItemMeta a = this.item.getItemMeta();
        return a.getLore();
    }

    /**
     * Add a flag to that item
     */
    public SItem flag(ItemFlag flag) {
        ItemMeta a = this.item.getItemMeta();
        a.addItemFlags(flag);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Removes a flag out of that item
     */
    public SItem removeFlag(ItemFlag flag) {
        ItemMeta a = this.item.getItemMeta();
        a.removeItemFlags(flag);
        this.item.setItemMeta(a);
        return this;
    }

    /**
     * Checks does a specific flag exist in that item
     */
    public Boolean hasFlag(ItemFlag flag) {
        ItemMeta a = this.item.getItemMeta();
        return a.hasItemFlag(flag);
    }

    /**
     * Gets all flag of that item
     */
    public Set<ItemFlag> flag() {
        ItemMeta a = this.item.getItemMeta();
        return a.getItemFlags();
    }

    /**
     * Sets a new durability value for that item
     */
    public SItem durability(short durability) {
        this.item.setDurability(durability);
        return this;
    }

    /**
     * Gets the durability value of that item
     */
    public short durability() {
        return this.item.getDurability();
    }

    /**
     * Sets a new type for that item
     */
    public SItem setType(Material type) {
        this.item.setType(type);
        return this;
    }

    /**
     * Gets the type of that item
     */
    public Material getType() {
        return this.item.getType();
    }

    /**
     * Sets amount value for that item
     */
    public SItem amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    /**
     * Gets amount value of that item
     */
    public int amount() {
        return this.item.getAmount();
    }

    /**
     * Gets that item as item stack
     *
     * @return item
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Toggles unbreakable of that item
     */
    public SItem unbreakable(Boolean b) {
        if(b) {
            item = new NBTManager(item).setBoolean("Unbreakable", true).toItemStack(item);
        } else {
            item = new NBTManager(item).remove("Unbreakable").toItemStack(item);
        }
        return this;
    }

    /**
     * Checks is that item unbreakable
     *
     * @return boolean
     */
    public Boolean isUnbreakable() {
        return new NBTManager(item).hasKey("Unbreakable");
    }

    /**
     * Adds an attribute to that item
     * @param type  type of attribute
     * @param value value
     */
    public SItem addAttribute(AttributeType type, double value) {
        List<Object> l = new NBTManager(item).getList("AttributeModifiers");
        if(l == null){
            l = new ArrayList<>();
        }
        String n = AttributeType.getModifierName(type);
        UUID uuid = UUID.randomUUID();
        NBTManager c = new NBTManager().set("AttributeName", n).set("Name", "Modifier").set("Amount", value).set("Operation", 0).set("UUIDLeast", uuid.getLeastSignificantBits()).set("UUIDMost", uuid.getMostSignificantBits());
        l.add(c.getWarpper());
        item = new NBTManager(item).setList("AttributeModifiers", l).toItemStack(item);
        return this;
    }

    /**
     * Adds an attribute to that item with a specific slot
     *  @param type type of attribute
     * @param value value
     * @param slot slot
     */
    public SItem addAttribute(AttributeType type, double value, EquipSlot slot) {
        List<Object> l = new NBTManager(item).getList("AttributeModifiers");
        if(l == null){
            l = new ArrayList<>();
        }
        String n = AttributeType.getModifierName(type);
        UUID uuid = UUID.randomUUID();
        NBTManager c = new NBTManager().set("AttributeName", n).set("Name", "Modifier").set("Amount", value).set("Operation", 0).set("UUIDLeast", uuid.getLeastSignificantBits()).set("UUIDMost", uuid.getMostSignificantBits()).set("Slot", slot.toString().toLowerCase());
        l.add(c.getWarpper());
        item = new NBTManager(item).setList("AttributeModifiers", l).toItemStack(item);
        return this;
    }

    /**
     * Sets a new attribute in a specific index of that item
     * If the attribute in that index doesn't exist, that attribute will be added automatically
     *  @param type  type of attribute
     * @param value value
     * @param index index
     */
    public SItem setAttribute(AttributeType type, double value, int index) {
        return removeAttribute(index).addAttribute(type, value);
    }

    /**
     * Sets a new attribute with a slot in a specific index of that item
     * If the attribute in that index doesn't exist, that attribute will be added automatically
     * @param type  type of attribute
     * @param value value
     * @param index index
     * @param slot slot
     */
    public SItem setAttribute(AttributeType type, double value, EquipSlot slot, int index) {
        return removeAttribute(index).addAttribute(type, value, slot);
    }

    /**
     * Removes an attribute which has a specific index out of that item
     *
     * @param index index of attribute
     */
    public SItem removeAttribute(int index) {
        List<Object> l = new NBTManager(item).getList("AttributeModifiers");
        if(l != null) {
            if(index < l.size()) {
                l.remove(index);
            }
            if(l.size() == 0){
                item = new NBTManager(item).remove("AttributeModifiers").toItemStack(item);
            } else {
                item = new NBTManager(item).setList("AttributeModifiers", l).toItemStack(item);
            }
        }
        return this;
    }

    /**
     * Removes all attributes which has a specific type out of that item
     *
     * @param type type of attribute
     */
    public SItem removeAttribute(AttributeType type) {
        List<Object> l = new NBTManager(item).getList("AttributeModifiers");
        if(l == null){
            l = new ArrayList<>();
        }
        List<Object> newCompounds = new ArrayList<>();
        String n = AttributeType.getModifierName(type);
        for(Object w : l){
            if(w instanceof NBTCompoundWrapper) {
                if(!((NBTCompoundWrapper) w).getString("AttributeName").equals(n)) {
                    newCompounds.add(w);
                }
            }
        }
        if(newCompounds.size() == 0){
            item = new NBTManager(item).remove("AttributeModifiers").toItemStack(item);
        } else {
            item = new NBTManager(item).setList("AttributeModifiers", newCompounds).toItemStack(item);
        }
        return this;
    }

    /**
     * Checks does that item have an attribute
     *
     * @param type  type of attribute
     */
    public Boolean hasAttribute(AttributeType type) {
        List<Object> l = new NBTManager(item).getList("AttributeModifiers");
        if(l == null){
            l = new ArrayList<>();
        }
        String n = AttributeType.getModifierName(type);
        for(Object w : l){
            if(w instanceof NBTCompoundWrapper) {
                if(((NBTCompoundWrapper) w).getString("AttributeName").equals(n)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets type of attribute which has a specific index
     *
     * @param index index of attribute
     *
     * @return type
     */
    public AttributeType getAttributeType(int index) {
        List<Object> l = new NBTManager(item).getList("AttributeModifiers");
        if(l == null){
            return null;
        } else {
            if(index < l.size()) {
                if(l.get(index) instanceof NBTCompoundWrapper) {
                    NBTCompoundWrapper w = (NBTCompoundWrapper) l.get(index);
                    return AttributeType.getByName(w.getString("AttributeName"));
                }
            }
        }
        return null;
    }

    /**
     * Gets amount of an attribute which has a specific type
     *
     * @param type type of attribute
     *
     * @return if attribute doesn't exist or it doesn't have "Amount" key, will return 0
     */
    public double getAttributeAmount(AttributeType type) {
        double r = 0;
        List<Object> l = new NBTManager(item).getList("AttributeModifiers");
        if(l == null){
            l = new ArrayList<>();
        }
        String n = AttributeType.getModifierName(type);
        for(Object w : l){
            if(w instanceof NBTCompoundWrapper) {
                if(((NBTCompoundWrapper) w).getString("AttributeName").equals(n) && ((NBTCompoundWrapper) w).hasKey("Amount")) {
                    r = ((NBTCompoundWrapper) w).getDouble("Amount");
                    break;
                }
            }
        }
        return r;
    }

    /**
     * Gets amount of an attribute which has a specific index
     *
     * @param index index of attribute
     *
     * @return if attribute doesn't exist or it doesn't have "Amount" key, will return 0
     */
    public int getAttributeAmount(int index) {
        int r = 0;
        List<Object> l = new NBTManager(item).getList("AttributeModifiers");
        if(l == null){
            l = new ArrayList<>();
        }
        if(index < l.size()) {
            if(l.get(index) instanceof NBTCompoundWrapper) {
                NBTCompoundWrapper w = (NBTCompoundWrapper) l.get(index);
                if(w.hasKey("Amount")) {
                    r = w.getInt("Amount");
                }
            }
        }
        return r;
    }

    /**
     * Gets slot of an attribute which has a specific index
     */
    public EquipSlot getAttributeEquipSlot(int index) {
        List<Object> l = new NBTManager(item).getList("AttributeModifiers");
        if(l != null) {
            if(index < l.size()) {
                if(l.get(index) instanceof NBTCompoundWrapper) {
                    NBTCompoundWrapper w = (NBTCompoundWrapper) l.get(index);
                    if(w.hasKey("Slot")) {
                        return EquipSlot.valueOf(w.getString("Slot").toUpperCase());
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets slot of an attribute which has a specific type
     */
    public EquipSlot getAttributeEquipSlot(AttributeType type) {
        String r = null;
        List<Object> l = new NBTManager(item).getList("AttributeModifiers");
        if(l == null){
            l = new ArrayList<>();
        }
        String n = AttributeType.getModifierName(type);
        for(Object w : l){
            if(w instanceof NBTCompoundWrapper) {
                if(((NBTCompoundWrapper) w).getString("AttributeName").equals(n) && ((NBTCompoundWrapper) w).hasKey("Slot")) {
                    r = ((NBTCompoundWrapper) w).getString("Slot").toUpperCase();
                    break;
                }
            }
        }
        return EquipSlot.valueOf(r);
    }

    /**
     * Gets all attributes of that item
     */
    public List<AttributeType> getAllAttributes() {
        List<AttributeType> types = new ArrayList<>();
        List<Object> l = new NBTManager(item).getList("AttributeModifiers");
        if(l == null){
            l = new ArrayList<>();
        }
        for(Object w : l){
            if(w instanceof NBTCompoundWrapper) {
                types.add(AttributeType.getByName(((NBTCompoundWrapper) w).getString("AttributeName")));
            }
        }
        return types;
    }
}
