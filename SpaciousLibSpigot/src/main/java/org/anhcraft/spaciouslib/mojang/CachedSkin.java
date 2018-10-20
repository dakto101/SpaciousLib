package org.anhcraft.spaciouslib.mojang;

import org.anhcraft.spaciouslib.annotations.DataField;
import org.anhcraft.spaciouslib.annotations.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

/**
 * Represents a cached player skin implementation.<br>
 * A cached skin is a skin but will be expired one time.<br>
 * The skin data is stored in the folder of this library.<br>
 * The skin will be renewed automatically when it is expired.
 */
@Serializable
public class CachedSkin {
    @DataField
    private int cachedTime;
    @DataField
    private long expiredTime;
    @DataField
    private Skin skin;
    @DataField
    private UUID owner;

    /**
     * Not recommended constructor, only be used during serialization processes
     */
    public CachedSkin(){}

    /**
     * Creates a new CachedSkin instance
     * @param skin Skin object
     * @param owner the unique id of the skin owner
     * @param cachedTime the cached time (in seconds)
     */
    public CachedSkin(Skin skin, UUID owner, int cachedTime) {
        this.skin = skin;
        this.owner = owner;
        this.cachedTime = cachedTime;
        this.expiredTime = cachedTime*1000 + System.currentTimeMillis();
    }

    /**
     * Creates a new CachedSkin instance
     * @param skin Skin object
     * @param owner the unique id of the skin owner
     * @param cachedTime the cached time (in seconds)
     * @param expiredTime the expired time (in milliseconds)
     */
    public CachedSkin(Skin skin, UUID owner, int cachedTime, long expiredTime) {
        this.skin = skin;
        this.owner = owner;
        this.cachedTime = cachedTime;
        this.expiredTime = expiredTime;
    }

    /**
     * Checks whether this skin expired
     * @return true if it expired
     */
    public boolean isExpired(){
        return System.currentTimeMillis() > this.expiredTime;
    }

    /**
     * Gets the cached time (in seconds)
     * @return the cached time
     */
    public int getCachedTime(){
        return this.cachedTime;
    }

    /**
     * Gets the expired time (in milliseconds)
     * @return the expired time
     */
    public long getExpiredTime(){
        return this.expiredTime;
    }

    /**
     * Gets the skin object
     * @return the skin object
     */
    public Skin getSkin(){
        return this.skin;
    }

    /**
     * Gets this skin owner
     * @return the skin owner
     */
    public UUID getOwner(){
        return this.owner;
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            CachedSkin s = (CachedSkin) o;
            return new EqualsBuilder()
                    .append(s.skin, this.skin)
                    .append(s.expiredTime, this.expiredTime)
                    .append(s.cachedTime, this.cachedTime)
                    .append(s.owner, this.owner)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(27, 33).append(this.skin).append(this.owner)
                .append(this.cachedTime).append(this.expiredTime).toHashCode();
    }
}