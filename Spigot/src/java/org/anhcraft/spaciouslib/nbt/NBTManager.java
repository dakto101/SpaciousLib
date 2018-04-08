package org.anhcraft.spaciouslib.nbt;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * A class helps you to load or apply NBT tags
 */
public class NBTManager {
    /**
     * Creates a new NBT Compound tag
     * @return NBTCompound object
     */
    public static NBTCompound newCompound(){
        try {
            Class<?> e = Class.forName("org.anhcraft.spaciouslib.nbt.NBTCompound_" +
                    GameVersion.getVersion().toString().replace("v", ""));
            Constructor c = e.getConstructor();
            return (NBTCompound) c.newInstance();
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * Loads all NBT tags from the given item
     * @param i the item
     * @return NBTCompound object
     */
    public static NBTCompound fromItem(ItemStack i){
        if(i == null){
            throw new NullPointerException();
        }
        try {
            Class<?> e = Class.forName("org.anhcraft.spaciouslib.nbt.NBTCompound_" +
                    GameVersion.getVersion().toString().replace("v", ""));
            Constructor c = e.getConstructor();
            NBTCompound compound = ((NBTCompound) c.newInstance());
            compound.fromItem(i.clone());
            return compound;
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * Loads all NBT tags from the given file
     * @param f the file
     * @return NBTCompound object
     */
    public static NBTCompound fromFile(File f){
        if(f == null){
            throw new NullPointerException();
        }
        try {
            Class<?> e = Class.forName("org.anhcraft.spaciouslib.nbt.NBTCompound_" +
                    GameVersion.getVersion().toString().replace("v", ""));
            Constructor c = e.getConstructor();
            NBTCompound compound = ((NBTCompound) c.newInstance());
            compound.fromFile(f);
            return compound;
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * Loads all NBT tags from the given entity
     * @param entity the entity
     * @return NBTCompound object
     */
    public static NBTCompound fromEntity(Entity entity){
        if(entity == null){
            throw new NullPointerException();
        }
        try {
            Class<?> e = Class.forName("org.anhcraft.spaciouslib.nbt.NBTCompound_" +
                    GameVersion.getVersion().toString().replace("v", ""));
            Constructor c = e.getConstructor();
            NBTCompound compound = ((NBTCompound) c.newInstance());
            compound.fromEntity(entity);
            return compound;
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * Loads all NBT tags from the given configuration section
     * @param configurationSection the configuration section
     * @return the NBTCompound object
     */
    public static NBTCompound fromConfigurationSection(ConfigurationSection configurationSection){
        NBTCompound nbt = newCompound();
        HashSet<String> sections = new HashSet<>();
        nbt = handlecs(nbt, configurationSection);
        for(String k : configurationSection.getKeys(true)){
            String[] css = k.split("\\.");
            if(1 < css.length){
                String sec = k.substring(0, k.length()-css[css.length-1].length()-1);
                sections.add(sec);
            }
        }
        for(String s : sections){
            nbt = handlecs(nbt, configurationSection.getConfigurationSection(s));
        }
        return nbt;
    }

    private static NBTCompound handlecs(NBTCompound nbt, ConfigurationSection configurationSection) {
        for(String k : configurationSection.getKeys(true)){
            String[] css = k.split("\\.");
            Object v = configurationSection.get(k);
            if(css.length == 1 && !(v instanceof MemorySection)){
                if(v instanceof List){
                    List<NBTCompound> compoundList = new ArrayList<>();
                    List<?> list = (List<?>) v;
                    if(0 < list.size() && list.get(0) instanceof ConfigurationSection){
                        int i = 0;
                        while(i < list.size()){
                            compoundList.add(fromConfigurationSection((ConfigurationSection) list.get(i)));
                            i++;
                        }
                    }
                    nbt = nbt.set(k, compoundList);
                } else {
                    nbt = nbt.set(k, v);
                }
            }
        }
        return nbt;
    }

    /**
     * Loads all NBT tags from the given JSON string
     * @param json the JSON string
     * @return the NBTCompound object
     */
    public static NBTCompound fromJSON(String json){
        if(!CommonUtils.isValidJSON(json)){
            return newCompound();
        }
        return handlejo(newCompound(), new Gson().fromJson(json, new TypeToken<JsonObject>(){}.getType()));
    }

    private static NBTCompound handlejo(NBTCompound nbtCompound, JsonObject json) {
        for(Map.Entry<String, JsonElement> data : json.entrySet()){
            nbtCompound.set(data.getKey(), handlejoe(data.getValue()));
        }
        return nbtCompound;
    }

    private static Object handlejoe(JsonElement e) {
        Object value = null;
        if(e.isJsonPrimitive()){
            JsonPrimitive p = e.getAsJsonPrimitive();
            if(p.isNumber()){
                value = p.getAsNumber();
            }
            else if(p.isBoolean()){
                value = p.getAsBoolean();
            }
            else if(p.isString()){
                value = p.getAsString();
            }
        }
        else if(e.isJsonArray()){
            List<Object> list = new ArrayList<>();
            JsonArray a = e.getAsJsonArray();
            for(JsonElement je : a) {
                list.add(handlejoe(je));
            }
            value = list;
        }
        else if(e.isJsonObject()){
            value = handlejo(newCompound(), e.getAsJsonObject());
        }
        return value;
    }
}