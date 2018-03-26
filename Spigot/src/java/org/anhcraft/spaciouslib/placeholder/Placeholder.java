package org.anhcraft.spaciouslib.placeholder;

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
     * Gets the data of this placeholder with the given player
     * @param player the player
     * @return the data of the placeholder
     */
    public abstract String getValue(Player player);
}