package org.anhcraft.spaciouslib.effects;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class LineEffect extends TargetedEffect {
    public LineEffect(Location location, Location target) {
        super(location, target);
    }

    public LineEffect(Location location, double distance) {
        super(location, distance);
    }

    @Override
    public void spawn() {
        double length = target.distance(location);
        double part = safeDivide(length, particleAmount);
        Vector dir = target.toVector().subtract(location.toVector());
        for(double x = 0; x < length; x += part){
            spawnParticle(location.add(rotate(dir.clone().normalize().multiply(x))));
        }
    }
}
