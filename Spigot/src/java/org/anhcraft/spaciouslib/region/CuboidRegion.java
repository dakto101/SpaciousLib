package org.anhcraft.spaciouslib.region;

import org.bukkit.Location;

/**
 * Represents a cuboid region implementation.
 */
public abstract class CuboidRegion {
    public abstract Location getPosition1();
    public abstract Location getPosition2();
}