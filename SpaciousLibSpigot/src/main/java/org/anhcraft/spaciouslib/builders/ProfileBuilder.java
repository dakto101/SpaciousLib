package org.anhcraft.spaciouslib.builders;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.anhcraft.spaciouslib.mojang.Skin;
import org.anhcraft.spaciouslib.utils.PlayerUtils;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Bukkit;

import java.util.UUID;

public class ProfileBuilder {
    private GameProfile gp;

    /**
     * Creates a new GameProfile
     * @param name profile name
     */
    public ProfileBuilder(String name){
        this.gp = new GameProfile(PlayerUtils.getOfflineId(name), name);
    }

    /**
     * Creates a new GameProfile
     * @param uuid profile id
     * @param name profile name
     */
    public ProfileBuilder(UUID uuid, String name){
        this.gp = new GameProfile(uuid, name);
        setLegacy(!Bukkit.getServer().getOnlineMode());
    }

    /**
     * Creates a new GameProfile
     * @param profile profile
     */
    public ProfileBuilder(GameProfile profile){
        this.gp = profile;
    }

    /**
     * Set the profile id
     * @param id profile id
     * @return this object
     */
    public ProfileBuilder setUniqueId(UUID id) {
        ReflectionUtils.setField("id", this.gp.getClass(), this.gp, id);
        return this;
    }

    /**
     * Set the properties map
     * @param properties properties map
     * @return this object
     */
    public ProfileBuilder setProperties(PropertyMap properties) {
        ReflectionUtils.setField("properties", this.gp.getClass(), this.gp, properties);
        return this;
    }

    /**
     * Set the legacy status
     * @param legacy legacy status
     * @return this object
     */
    public ProfileBuilder setLegacy(boolean legacy) {
        ReflectionUtils.setField("legacy", this.gp.getClass(), this.gp, legacy);
        return this;
    }

    /**
     * Set the profile name
     * @param name profile name
     * @return this object
     */
    public ProfileBuilder setName(String name) {
        ReflectionUtils.setField("name", this.gp.getClass(), this.gp, name);
        return this;
    }

    /**
     * Set the skin for the profile
     * @param skin skin
     * @return this object
     */
    public ProfileBuilder setSkin(Skin skin){
        this.gp.getProperties().removeAll("textures");
        this.gp.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
        return this;
    }

    /**
     * Build this profile
     * @return the profile
     */
    public GameProfile build(){
        return this.gp;
    }
}
