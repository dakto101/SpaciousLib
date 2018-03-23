package org.anhcraft.spaciouslib.nbt;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;

public class NBTManager {
    private NBTCompoundWrapper wrapper;

    /**
     * Creates a new NBTCompound
     */
    public NBTManager(){
        try {
            Class<?> e = Class.forName("org.anhcraft.spaciouslib.nbt.NBTCompound_" +
                    GameVersion.getVersion().toString().replace("v", ""));
            Constructor c = e.getConstructor();
            wrapper = (NBTCompoundWrapper) c.newInstance();
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Loads all NBT tags from an item
     * @param i item
     */
    public NBTManager(ItemStack i){
        try {
            Class<?> e = Class.forName("org.anhcraft.spaciouslib.nbt.NBTCompound_" +
                    GameVersion.getVersion().toString().replace("v", ""));
            Constructor c = e.getConstructor();
            wrapper = (NBTCompoundWrapper) c.newInstance();
            wrapper.fromItem(i.clone());
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Loads all NBT tags from a file
     * @param f file
     */
    public NBTManager(File f){
        try {
            Class<?> e = Class.forName("org.anhcraft.spaciouslib.nbt.NBTCompound_" +
                    GameVersion.getVersion().toString().replace("v", ""));
            Constructor c = e.getConstructor();
            wrapper = (NBTCompoundWrapper) c.newInstance();
            wrapper.fromFile(f);
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Loads all NBT tags from an entity
     * @param entity entity
     */
    public NBTManager(Entity entity){
        try {
            Class<?> e = Class.forName("org.anhcraft.spaciouslib.nbt.NBTCompound_" +
                    GameVersion.getVersion().toString().replace("v", ""));
            Constructor c = e.getConstructor();
            wrapper = (NBTCompoundWrapper) c.newInstance();
            wrapper.fromEntity(entity);
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Loads all NBT tags from NBTCompoundWrapper instance
     * @param wrapper NBTCompoundWrapper instance
     */
    public NBTManager(NBTCompoundWrapper wrapper){
        this.wrapper = wrapper;
    }
    
    public NBTManager setString(String name, String value) {
        wrapper.map.put(name, value);
        return this;
    }

    public NBTManager setInt(String name, int value) {
        wrapper.map.put(name, value);
        return this;
    }

    public NBTManager setBoolean(String name, Boolean value) {
        wrapper.map.put(name, value);
        return this;
    }

    public NBTManager setShort(String name, short value) {
        wrapper.map.put(name, value);
        return this;
    }

    public NBTManager setDouble(String name, double value) {
        wrapper.map.put(name, value);
        return this;
    }

    public NBTManager setFloat(String name, float value) {
        wrapper.map.put(name, value);
        return this;
    }

    public NBTManager setLong(String name, long value) {
        wrapper.map.put(name, value);
        return this;
    }

    public NBTManager setByte(String name, byte value) {
        wrapper.map.put(name, value);
        return this;
    }

    public NBTManager setIntArray(String name, int[] value) {
        wrapper.map.put(name, value);
        return this;
    }

    public NBTManager setByteArray(String name, byte[] value) {
        wrapper.map.put(name, value);
        return this;
    }

    public <E> NBTManager setList(String name, List<E> value) {
        wrapper.map.put(name, value);
        return this;
    }

    public NBTManager set(String name, Object value) {
        wrapper.map.put(name, value);
        return this;
    }

    public String getString(String name) {
        return (String) wrapper.map.get(name);
    }

    public int getInt(String name) {
        return (int) wrapper.map.get(name);
    }

    public Boolean getBoolean(String name) {
        return (Boolean) wrapper.map.get(name);
    }

    public short getShort(String name) {
        return (short) wrapper.map.get(name);
    }

    public double getDouble(String name) {
        return (double) wrapper.map.get(name);
    }

    public float getFloat(String name) {
        return (float) wrapper.map.get(name);
    }

    public long getLong(String name) {
        return (long) wrapper.map.get(name);
    }

    public byte getByte(String name) {
        return (byte) wrapper.map.get(name);
    }

    public int[] getIntArray(String name) {
        return (int[]) wrapper.map.get(name);
    }

    public byte[] getByteArray(String name) {
        return (byte[]) wrapper.map.get(name);
    }

    public <E> List<E> getList(String name) {
        return (List<E>) wrapper.map.get(name);
    }

    public void setTags(LinkedHashMap<String, Object> tags) {
        wrapper.map = tags;
    }

    public LinkedHashMap<String, Object> getTags() {
        return wrapper.map;
    }

    public void setCompound(NBTCompoundWrapper compound, String name) {
        wrapper.map.put(name, compound);
    }

    public NBTCompoundWrapper getCompound(String name) {
        return (NBTCompoundWrapper) wrapper.map.get(name);
    }

    public NBTCompoundWrapper getWrapper(){
        return wrapper;
    }

    public NBTManager remove(String name) {
        wrapper.map.remove(name);
        return this;
    }

    public Boolean hasKey(String key) {
        return wrapper.map.containsKey(key);
    }

    public Boolean hasValue(String value) {
        return wrapper.map.containsValue(value);
    }

    /**
     * Overrides all NBT tag of an item
     * @param oldItem old version of that item
     * @return new version of that item
     */
    public ItemStack toItemStack(ItemStack oldItem) {
        return wrapper.toItem(oldItem);
    }

    /**
     * Overrides all NBT tag of a file
     * @param file file
     */
    public void toFile(File file) {
        wrapper.toFile(file);
    }

    /**
     * Overrides all NBT tag of an entity
     * @param entity entity
     */
    public void toEntity(Entity entity) {
        wrapper.toEntity(entity);
    }
}