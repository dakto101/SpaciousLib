package org.anhcraft.spaciouslib.nbt;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
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

    public Object get(String name) {
        return tags.get(name);
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

    public NBTCompound setCompound(String name, NBTCompound compound) {
        tags.put(name, compound);
        return this;
    }

    public NBTCompound getCompound(String name) {
        return (NBTCompound) tags.get(name);
    }

    public NBTCompound setTags(LinkedHashMap<String, Object> tags) {
        this.tags = tags;
        return this;
    }

    public LinkedHashMap<String, Object> getTags() {
        return tags;
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

    /**
     * Saves all NBT tags to the given configuration section
     * @param configurationSection configuration section
     */
    public void toConfigurationSection(ConfigurationSection configurationSection){
        handle2cs(configurationSection, this);
    }

    private static void handle2cs(ConfigurationSection configurationSection, NBTCompound nbtCompound) {
        for(String k : nbtCompound.tags.keySet()){
            Object v = nbtCompound.tags.get(k);
            if(v instanceof NBTCompound){
                handle2cs(configurationSection.createSection(k), (NBTCompound) v);
            } else if(v instanceof List){
                List<?> vlist = (List<?>) v;
                if(0 < vlist.size() && vlist.get(0) instanceof NBTCompound) {
                    List<ConfigurationSection> cs = new ArrayList<>();
                    int i = 0;
                    while(i < vlist.size()){
                        ConfigurationSection c = new YamlConfiguration();
                        handle2cs(c, (NBTCompound) ((List) v).get(i));
                        cs.add(c);
                        i++;
                    }
                    configurationSection.set(k, cs);
                } else {
                    configurationSection.set(k, v);
                }
            } else {
                configurationSection.set(k, v);
            }
        }
    }

    /**
     * Saves all NBT tags to JSON string
     * @return the JSON string
     */
    public String toJSON(){
        JsonObject jo = new JsonObject();
        handle2jo(jo, this);
        return new Gson().toJson(jo, new TypeToken<JsonObject>(){}.getType());
    }

    private void handle2jo(JsonObject jsonObject, NBTCompound nbtCompound) {
        for(String k : nbtCompound.tags.keySet()){
            Object v = nbtCompound.tags.get(k);
            if(v instanceof NBTCompound){
                JsonObject jo = new JsonObject();
                handle2jo(jo, (NBTCompound) v);
                jsonObject.add(k, jo);
            } else if(v instanceof List){
                List<?> vlist = (List<?>) v;
                if(0 < vlist.size()) {
                    JsonArray array = new JsonArray();
                    if(vlist.get(0) instanceof NBTCompound) {
                        int i = 0;
                        while(i < vlist.size()) {
                            JsonObject jo = new JsonObject();
                            handle2jo(jo, (NBTCompound) ((List) v).get(i));
                            array.add(jo);
                            i++;
                        }
                    }
                    else if(vlist.get(0) instanceof Number){
                        int i = 0;
                        while(i < vlist.size()) {
                            array.add(new JsonPrimitive((Number) ((List) v).get(i)));
                            i++;
                        }
                    }
                    else if(vlist.get(0) instanceof Boolean){
                        int i = 0;
                        while(i < vlist.size()) {
                            array.add(new JsonPrimitive((Boolean) ((List) v).get(i)));
                            i++;
                        }
                    }
                    else if(vlist.get(0) instanceof String){
                        int i = 0;
                        while(i < vlist.size()) {
                            array.add(new JsonPrimitive((String) ((List) v).get(i)));
                            i++;
                        }
                    }
                    else if(vlist.get(0) instanceof Character){
                        int i = 0;
                        while(i < vlist.size()) {
                            array.add(new JsonPrimitive((Character) ((List) v).get(i)));
                            i++;
                        }
                    }
                    jsonObject.add(k, array);
                }
            } else {
                if(v instanceof Number){
                    jsonObject.addProperty(k, (Number) v);
                }
                if(v instanceof Boolean){
                    jsonObject.addProperty(k, (Boolean) v);
                }
                if(v instanceof String){
                    jsonObject.addProperty(k, (String) v);
                }
                if(v instanceof Character){
                    jsonObject.addProperty(k, (Character) v);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            NBTCompound nbt = (NBTCompound) o;
            return new EqualsBuilder()
                    .append(nbt.tags, this.tags)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(33, 21)
                .append(tags).toHashCode();
    }
}