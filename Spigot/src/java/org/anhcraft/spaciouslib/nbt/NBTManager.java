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
        if(i == null){
            throw new NullPointerException();
        }
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
        if(f == null){
            throw new NullPointerException();
        }
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
        if(entity == null){
            throw new NullPointerException();
        }
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
        wrapper.tags.put(name, value);
        return this;
    }

    public NBTManager setInt(String name, int value) {
        wrapper.tags.put(name, value);
        return this;
    }

    public NBTManager setBoolean(String name, Boolean value) {
        wrapper.tags.put(name, value);
        return this;
    }

    public NBTManager setShort(String name, short value) {
        wrapper.tags.put(name, value);
        return this;
    }

    public NBTManager setDouble(String name, double value) {
        wrapper.tags.put(name, value);
        return this;
    }

    public NBTManager setFloat(String name, float value) {
        wrapper.tags.put(name, value);
        return this;
    }

    public NBTManager setLong(String name, long value) {
        wrapper.tags.put(name, value);
        return this;
    }

    public NBTManager setByte(String name, byte value) {
        wrapper.tags.put(name, value);
        return this;
    }

    public NBTManager setIntArray(String name, int[] value) {
        wrapper.tags.put(name, value);
        return this;
    }

    public NBTManager setByteArray(String name, byte[] value) {
        wrapper.tags.put(name, value);
        return this;
    }

    public <E> NBTManager setList(String name, List<E> value) {
        wrapper.tags.put(name, value);
        return this;
    }

    public NBTManager set(String name, Object value) {
        wrapper.tags.put(name, value);
        return this;
    }

    public String getString(String name) {
        return (String) wrapper.tags.get(name);
    }

    public int getInt(String name) {
        return (int) wrapper.tags.get(name);
    }

    public Boolean getBoolean(String name) {
        return (Boolean) wrapper.tags.get(name);
    }

    public short getShort(String name) {
        return (short) wrapper.tags.get(name);
    }

    public double getDouble(String name) {
        return (double) wrapper.tags.get(name);
    }

    public float getFloat(String name) {
        return (float) wrapper.tags.get(name);
    }

    public long getLong(String name) {
        return (long) wrapper.tags.get(name);
    }

    public byte getByte(String name) {
        return (byte) wrapper.tags.get(name);
    }

    public int[] getIntArray(String name) {
        return (int[]) wrapper.tags.get(name);
    }

    public byte[] getByteArray(String name) {
        return (byte[]) wrapper.tags.get(name);
    }

    public <E> List<E> getList(String name) {
        return (List<E>) wrapper.tags.get(name);
    }

    public void setTags(LinkedHashMap<String, Object> tags) {
        wrapper.tags = tags;
    }

    public LinkedHashMap<String, Object> getTags() {
        return wrapper.tags;
    }

    public void setCompound(NBTCompoundWrapper compound, String name) {
        wrapper.tags.put(name, compound);
    }

    public NBTCompoundWrapper getCompound(String name) {
        return (NBTCompoundWrapper) wrapper.tags.get(name);
    }

    public NBTCompoundWrapper getWrapper(){
        return wrapper;
    }

    public NBTManager remove(String name) {
        wrapper.tags.remove(name);
        return this;
    }

    public Boolean hasKey(String key) {
        return wrapper.tags.containsKey(key);
    }

    public Boolean hasValue(String value) {
        return wrapper.tags.containsValue(value);
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