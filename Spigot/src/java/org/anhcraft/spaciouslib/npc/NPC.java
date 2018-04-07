package org.anhcraft.spaciouslib.npc;

import com.mojang.authlib.GameProfile;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Location;

import java.util.List;

/**
 * Represents a NPC (fake online players) implementation.
 */
public class NPC {
    public enum Addition {
        /**
         * Enables the interact handler, it allows you to use the NPCInteractEvent event.
         */
        INTERACT_HANDLER,
        /**
         * Enables the feature that makes NPCs always look the nearest viewer
         */
        LOOK_VIEWER,
        /**
         * Enables the feature that only show the NPC for its nearby players
         */
        NEARBY_RENDER
    }


    private GameProfile gameProfile;
    private List<Addition> additions;
    protected Location location;
    private double nearbyRadius;

    /**
     * Sets the nearby radius for the NEARBY_RENDER addition
     * @param nearbyRadius the radius
     * @return this object
     */
    public NPC setNearbyRadius(double nearbyRadius){
        this.nearbyRadius = nearbyRadius;
        return this;
    }

    public List<Addition> getAdditions() {
        return this.additions;
    }

    public Location getLocation() {
        return this.location;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public double getNearbyRadius() {
        return this.nearbyRadius;
    }

    /**
     * Creates a new NPC instance
     * @param gameProfile the game profile of the NPC
     * @param location the location of the NPC
     * @param additions all additions for the NPC
     */
    public NPC(GameProfile gameProfile, Location location, Addition... additions){
        this.gameProfile = gameProfile;
        this.location = location;
        this.additions = CommonUtils.toList(additions);
        this.nearbyRadius = 100;
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            NPC n = (NPC) o;
            return new EqualsBuilder()
                    .append(n.additions, this.additions)
                    .append(n.gameProfile, this.gameProfile)
                    .append(n.location, this.location)
                    .append(n.nearbyRadius, this.nearbyRadius)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(6, 17).append(this.additions)
                .append(this.gameProfile).append(this.location).append(this.nearbyRadius).toHashCode();
    }
}
