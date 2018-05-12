package org.anhcraft.spaciouslib.effects;

import org.bukkit.Location;

public abstract class TargetedEffect extends Effect {
    protected Location target;

    public TargetedEffect(Location location, Location target) {
        super(location);
        setTarget(target);
    }

    public TargetedEffect(Location location, double distance) {
        super(location);
        setTarget(distance);
    }

    public Location getTarget() {
        return target;
    }

    public void setTarget(Location target) {
        this.target = target;
    }

    public void setTarget(double distance) {
        Location loc = location.clone();
        this.target = loc.add(loc.getDirection().normalize().multiply(distance));
    }
}
