package org.anhcraft.spaciouslib.mojang;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class GameProfileBuilder {
    private GameProfile gp;

    public GameProfileBuilder(String name){
        this.gp = new GameProfile(UUID.randomUUID(), name);
    }

    public GameProfileBuilder(UUID uuid, String name){
        this.gp = new GameProfile(uuid, name);
        setLegacy(!Bukkit.getServer().getOnlineMode());
    }

    public GameProfileBuilder(HumanEntity entity){
        try {
            Class<?> craftHumanEntityClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".entity.CraftHumanEntity");
            Class<?> nmsEntityHumanClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".EntityHuman");
            Object craftHumanEntity = craftHumanEntityClass.cast(entity);
            Method handleMethod = craftHumanEntityClass.getDeclaredMethod("getHandle");
            Object entityHuman = handleMethod.invoke(craftHumanEntity);
            Method profile = nmsEntityHumanClass.getDeclaredMethod("getProfile");
            GameProfile gp = (GameProfile) profile.invoke(entityHuman);
            this.gp = new GameProfile(gp.getId(), gp.getName());
            setProperties(gp.getProperties());
            setLegacy(gp.isLegacy());
        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public GameProfileBuilder setID(UUID id) {
        try {
            Field f = this.gp.getClass().getDeclaredField("id");
            f.setAccessible(true);
            f.set(this.gp, id);
        } catch(IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return this;
    }

    public GameProfileBuilder setProperties(PropertyMap properties) {
        try {
            Field f = this.gp.getClass().getDeclaredField("properties");
            f.setAccessible(true);
            f.set(this.gp, properties);
        } catch(IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return this;
    }

    public GameProfileBuilder setLegacy(boolean legacy) {
        try {
            Field f = this.gp.getClass().getDeclaredField("legacy");
            f.setAccessible(true);
            f.set(this.gp, legacy);
        } catch(IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return this;
    }

    public GameProfileBuilder setName(String name) {
        try {
            Field f = this.gp.getClass().getDeclaredField("name");
            f.setAccessible(true);
            f.set(this.gp, name);
        } catch(IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return this;
    }

    public GameProfileBuilder setSkin(Skin skin){
        this.gp.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
        return this;
    }

    public GameProfile getGameProfile(){
        return this.gp;
    }
}
