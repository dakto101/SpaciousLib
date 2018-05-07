package org.anhcraft.spaciouslib.placeholder;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
     * Gets the data of this placeholder with the given player
     * @param player the player
     * @return the data of the placeholder
     */
    public abstract String getValue(ProxiedPlayer player);

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