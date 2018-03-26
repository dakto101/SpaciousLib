package org.anhcraft.spaciouslib.nbt;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Represents a NBT Compound tag implementation.
 */
public abstract class NBTCompound {
    protected LinkedHashMap<String, Object> tags = new LinkedHashMap<>();

    protected abstract void fromItem(ItemStack item);

    /**
     * Overrides all NBT tags into the given item
     * @param item old version of the item
     * @return new version of the item
     */
    public abstract ItemStack toItem(ItemStack item);

    protected abstract void fromFile(File file);

    /**
     * Saves all NBT tags to the given file
     * @param file file
     */
    public abstract void toFile(File file);

    protected abstract void fromEntity(Entity entity);

    /**
     * Saves all NBT tags to the given entity
     * @param entity entity
     */
    public abstract void toEntity(Entity entity);
    
    public NBTCompound setString(String name, String value) {
        tags.put(name, value);
        return this;
    }

    public NBTCompound setInt(String name, int value) {
        tags.put(name, value);
        return this;
    }

    public NBTCompound setBoolean(String name, Boolean value) {
        tags.put(name, value);
        return this;
    }

    public NBTCompound setShort(String name, short value) {
        tags.put(name, value);
        return this;
    }

    public NBTCompound setDouble(String name, double value) {
        tags.put(name, value);
        return this;
    }

    public NBTCompound setFloat(String name, float value) {
        tags.put(name, value);
        return this;
    }

    public NBTCompound setLong(String name, long value) {
        tags.put(name, value);
        return this;
    }

    public NBTCompound setByte(String name, byte value) {
        tags.put(name, value);
        return this;
    }

    public NBTCompound setIntArray(String name, int[] value) {
        tags.put(name, value);
        return this;
    }

    public NBTCompound setByteArray(String name, byte[] value) {
        tags.put(name, value);
        return this;
    }

    public <E> NBTCompound setList(String name, List<E> value) {
        tags.put(name, value);
        return this;
    }

    public NBTCompound set(String name, Object value) {
        tags.put(name, value);
        return this;
    }

    public String getString(String name) {
        return (String) tags.get(name);
    }

    public int getInt(String name) {
        return (int) tags.get(name);
    }

    public Boolean getBoolean(String name) {
        return (Boolean) tags.get(name);
    }

    public short getShort(String name) {
        return (short) tags.get(name);
    }

    public double getDouble(String name) {
        return (double) tags.get(name);
    }

    public float getFloat(String name) {
        return (float) tags.get(name);
    }

    public long getLong(String name) {
        return (long) tags.get(name);
    }

    public byte getByte(String name) {
        return (byte) tags.get(name);
    }

    public int[] getIntArray(String name) {
        return (int[]) tags.get(name);
    }

    public byte[] getByteArray(String name) {
        return (byte[]) tags.get(name);
    }

    public <E> List<E> getList(String name) {
        return (List<E>) tags.get(name);
    }

    public void setTags(LinkedHashMap<String, Object> tags) {
        tags = tags;
    }

    public LinkedHashMap<String, Object> getTags() {
        return tags;
    }

    public void setCompound(NBTCompound compound, String name) {
        tags.put(name, compound);
    }

    public NBTCompound getCompound(String name) {
        return (NBTCompound) tags.get(name);
    }

    public NBTCompound remove(String name) {
        tags.remove(name);
        return this;
    }

    public Boolean hasKey(String key) {
        return tags.containsKey(key);
    }

    public Boolean hasValue(String value) {
        return tags.containsValue(value);
    }
}