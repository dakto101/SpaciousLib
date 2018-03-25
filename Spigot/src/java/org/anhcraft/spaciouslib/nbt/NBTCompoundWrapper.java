package org.anhcraft.spaciouslib.nbt;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.LinkedHashMap;

public abstract class NBTCompoundWrapper {
    protected LinkedHashMap<String, Object> tags = new LinkedHashMap<>();

    public abstract void fromItem(ItemStack item);
    public abstract ItemStack toItem(ItemStack item);
    public abstract void fromFile(File file);
    public abstract void toFile(File file);
    public abstract void fromEntity(Entity entity);
    public abstract void toEntity(Entity entity);
}