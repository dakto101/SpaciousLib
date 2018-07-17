package org.anhcraft.spaciouslib.placeholder;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.entity.Player;

/**
 * Represents a placeholder implementation.<br>
 * Warning: If you replace the placeholder, you will have the newest data so there is a problem with performance.<br>
 * Recommend: Uses FixedPlaceholder or CachedPlaceholder instead.
 */
public abstract class Placeholder {
    /**
     * Gets the placeholder (e.g: {player_name})
     * @return the placeholder
     */
    public abstract String getPlaceholder();

    /**
     * Gets the replaced text for the given player
     * @param player the player
     * @return the replaced text, returns null if you want to prevent replace
     */
    public abstract String getValue(Player player);

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            Placeholder p = (Placeholder) o;
            return new EqualsBuilder()
                    .append(p.getPlaceholder(), this.getPlaceholder())
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(39, 11)
                .append(getPlaceholder()).toHashCode();
    }
}