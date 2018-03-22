package org.anhcraft.spaciouslib.nbt;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

public interface NBTCompoundWrapper {
    void fromItem(ItemStack item);
    ItemStack toItem(ItemStack item);
    void fromFile(File file);
    void toFile(File file);
    void fromEntity(Entity entity);
    void toEntity(Entity entity);
    void set(String name, Object value);
    String getString(String name);
    int getInt(String name);
    Boolean getBoolean(String name);
    short getShort(String name);
    double getDouble(String name);
    float getFloat(String name);
    long getLong(String name);
    byte getByte(String name);
    int[] getIntArray(String name);
    byte[] getByteArray(String name);
    List<Object> getList(String name);
    NBTCompoundWrapper getCompound(String name);
    void remove(String name);
    Boolean hasKey(String key);
    Boolean hasValue(String value);
    LinkedHashMap<String, Object> getAllTags();
}
