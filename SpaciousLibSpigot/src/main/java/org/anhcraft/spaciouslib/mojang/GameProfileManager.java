package org.anhcraft.spaciouslib.mojang;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * A class helps you to manage game profiles
 */
public class GameProfileManager {
    private GameProfile gp;

    /**
     * Creates a new GameProfileManager instance
     * @param name the name of the profile
     */
    public GameProfileManager(String name){
        this.gp = new GameProfile(UUID.randomUUID(), name);
    }

    /**
     * Creates a new GameProfileManager instance
     * @param uuid the unique id of the profile
     * @param name the name of the profile
     */
    public GameProfileManager(UUID uuid, String name){
        this.gp = new GameProfile(uuid, name);
        setLegacy(!Bukkit.getServer().getOnlineMode());
    }

    /**
     * Creates a new GameProfileManager instance from a GameProfile object
     * @param gp the game profile
     */
    public GameProfileManager(GameProfile gp){
        this.gp = gp;
    }

    /**
     * Creates a new GameProfileManager instance from the game profile of the given human entity
     * @param entity the human entity
     */
    public GameProfileManager(HumanEntity entity){
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> craftHumanEntityClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftHumanEntity");
            Class<?> nmsEntityHumanClass = Class.forName("net.minecraft.server." + v + ".EntityHuman");
            Object craftHumanEntity = ReflectionUtils.cast(craftHumanEntityClass, entity);
            Object entityHuman = ReflectionUtils.getMethod("getHandle", craftHumanEntityClass, craftHumanEntity);
            GameProfile gp = (GameProfile) ReflectionUtils.getMethod("getProfile", nmsEntityHumanClass, entityHuman);
            this.gp = new GameProfile(gp.getId(), gp.getName());
            setProperties(gp.getProperties());
            setLegacy(gp.isLegacy());
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set a new ID for this profile
     * @param id UUID object
     * @return this object
     */
    public GameProfileManager setUniqueId(UUID id) {
        ReflectionUtils.setField("id", this.gp.getClass(), this.gp, id);
        return this;
    }

    /**
     * Get the ID of this profile
     * @return UUID object
     */
    public UUID getUniqueId() {
        return (UUID) ReflectionUtils.getField("id", this.gp.getClass(), this.gp);
    }

    /**
     * Set a new properties map for this profile
     * @param properties the properties map
     * @return this object
     */
    public GameProfileManager setProperties(PropertyMap properties) {
        ReflectionUtils.setField("properties", this.gp.getClass(), this.gp, properties);
        return this;
    }

    /**
     * Set a new legacy value for this profile
     * @param legacy the legacy value
     * @return this object
     */
    public GameProfileManager setLegacy(boolean legacy) {
        ReflectionUtils.setField("legacy", this.gp.getClass(), this.gp, legacy);
        return this;
    }

    /**
     * Check whether this profile is legacy or not
     * @return true if yes
     */
    public boolean isLegacy() {
        return (boolean) ReflectionUtils.getField("legacy", this.gp.getClass(), this.gp);
    }

    /**
     * Set a new name for this profile
     * @param name the name
     * @return this object
     */
    public GameProfileManager setName(String name) {
        ReflectionUtils.setField("name", this.gp.getClass(), this.gp, name);
        return this;
    }

    /**
     * Get the profile name
     * @return the name
     */
    public String getName() {
        return (String) ReflectionUtils.getField("name", this.gp.getClass(), this.gp);
    }

    /**
     * Set a new skin for this profile
     * @param skin Skin object
     * @return this object
     */
    public GameProfileManager setSkin(Skin skin){
        // removes the key because the "put" method can only use one time
        this.gp.getProperties().removeAll("textures");
        // put the value as the first element of the texture values
        this.gp.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
        return this;
    }

    /**
     * Get the skin
     * @return Skin object
     */
    public Skin getSkin(){
        if(this.gp.getProperties().containsKey("textures")){
            Collection<Property> v = this.gp.getProperties().get("textures");
            if(v.size() > 0){
                Property s = v.iterator().next();
                return new Skin(s.getValue(), s.getSignature());
            }
        }
        return null;
    }

    /**
     * Applies this game profile to the human entity.<br>
     * Warning: you have to request the player client to reload the player
     * @param entity the human entity
     * @return this object
     */
    public GameProfileManager apply(HumanEntity entity){
        String v = GameVersion.getVersion().toString();
        LinkedHashMap<GameVersion, String> vars = new LinkedHashMap<>();
        vars.put(GameVersion.v1_8_R1, "bF");
        vars.put(GameVersion.v1_8_R2, "bH");
        vars.put(GameVersion.v1_8_R3, "bH");
        vars.put(GameVersion.v1_9_R1, "bR");
        vars.put(GameVersion.v1_9_R2, "bS");
        vars.put(GameVersion.v1_10_R1, "bT");
        vars.put(GameVersion.v1_11_R1, "bS");
        vars.put(GameVersion.v1_12_R1, "g");
        vars.put(GameVersion.v1_13_R1, "h");
        vars.put(GameVersion.v1_13_R2, "h");
        try {
            Class<?> craftHumanEntityClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftHumanEntity");
            Class<?> nmsEntityHumanClass = Class.forName("net.minecraft.server." + v + ".EntityHuman");
            Object craftHumanEntity = ReflectionUtils.cast(craftHumanEntityClass, entity);
            Object entityHuman = ReflectionUtils.getMethod("getHandle", craftHumanEntityClass, craftHumanEntity);
            ReflectionUtils.setField(vars.get(GameVersion.getVersion()), nmsEntityHumanClass, entityHuman, this.gp);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Get the result as a game profile object
     * @return the game profile object
     */
    public GameProfile getGameProfile(){
        return this.gp;
    }
}
