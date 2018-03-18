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
    private NBTCompoundWrapper warpper;

    /**
     * Creates a new NBTCompound
     */
    public NBTManager(){
        try {
            Class<?> e = Class.forName("org.anhcraft.spaciouslib.nbt.NBTCompound_" +
                    GameVersion.getVersion().toString().replace("v", ""));
            Constructor c = e.getConstructor();
            warpper = (NBTCompoundWrapper) c.newInstance();
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
            warpper = (NBTCompoundWrapper) c.newInstance();
            warpper.fromItem(i.clone());
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
            warpper = (NBTCompoundWrapper) c.newInstance();
            warpper.fromFile(f);
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
            warpper = (NBTCompoundWrapper) c.newInstance();
            warpper.fromEntity(entity);
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Loads all NBT tags from NBTCompoundWrapper instance
     * @param warpper NBTCompoundWrapper instance
     */
    public NBTManager(NBTCompoundWrapper warpper){
        this.warpper = warpper;
    }

    public NBTManager set(String name, Object value) {
        warpper.set(name, value);
        return this;
    }
    
    public NBTManager setString(String name, String value) {
        warpper.set(name, value);
        return this;
    }

    public NBTManager setInt(String name, int value) {
        warpper.set(name, value);
        return this;
    }

    public NBTManager setBoolean(String name, Boolean value) {
        warpper.set(name, value);
        return this;
    }

    public NBTManager setShort(String name, short value) {
        warpper.set(name, value);
        return this;
    }

    public NBTManager setDouble(String name, double value) {
        warpper.set(name, value);
        return this;
    }

    public NBTManager setFloat(String name, float value) {
        warpper.set(name, value);
        return this;
    }

    public NBTManager setLong(String name, long value) {
        warpper.set(name, value);
        return this;
    }

    public NBTManager setByte(String name, byte value) {
        warpper.set(name, value);
        return this;
    }

    public NBTManager setIntArray(String name, int[] value) {
        warpper.set(name, value);
        return this;
    }

    public NBTManager setByteArray(String name, byte[] value) {
        warpper.set(name, value);
        return this;
    }

    public NBTManager setList(String name, List<NBTCompoundWrapper> value) {
        warpper.set(name, value);
        return this;
    }

    public String getString(String name) {
        return warpper.getString(name);
    }

    public int getInt(String name) {
        return warpper.getInt(name);
    }

    public Boolean getBoolean(String name) {
        return warpper.getBoolean(name);
    }

    public short getShort(String name) {
        return warpper.getShort(name);
    }

    public double getDouble(String name) {
        return warpper.getDouble(name);
    }

    public float getFloat(String name) {
        return warpper.getFloat(name);
    }

    public long getLong(String name) {
        return warpper.getLong(name);
    }

    public byte getByte(String name) {
        return warpper.getByte(name);
    }

    public int[] getIntArray(String name) {
        return warpper.getIntArray(name);
    }

    public byte[] getByteArray(String name) {
        return warpper.getByteArray(name);
    }

    public List<NBTCompoundWrapper> getList(String name) {
        return warpper.getList(name);
    }

    public LinkedHashMap<String, Object> getAllTags() {
        return warpper.getAllTags();
    }

    public NBTCompoundWrapper getCompound(String name) {
        return warpper.getCompound(name);
    }

    public NBTCompoundWrapper getWarpper(){
        return warpper;
    }

    public NBTManager remove(String name) {
        warpper.remove(name);
        return this;
    }

    public Boolean hasKey(String key) {
        return warpper.hasKey(key);
    }

    public Boolean hasValue(String value) {
        return warpper.hasValue(value);
    }

    /**
     * Overrides all NBT tag of an item
     * @param oldItem old version of that item
     * @return new version of that item
     */
    public ItemStack toItemStack(ItemStack oldItem) {
        return warpper.toItem(oldItem);
    }

    /**
     * Overrides all NBT tag of a file
     * @param file file
     */
    public void toFile(File file) {
        warpper.toFile(file);
    }

    /**
     * Overrides all NBT tag of an entity
     * @param entity entity
     */
    public void toEntity(Entity entity) {
        warpper.toEntity(entity);
    }
}