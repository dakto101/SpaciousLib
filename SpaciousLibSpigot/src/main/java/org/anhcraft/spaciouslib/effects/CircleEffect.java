package org.anhcraft.spaciouslib.effects;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class CircleEffect extends Effect {
    private double radius;

    public CircleEffect(Location location) {
        super(location);
        radius = 1;
    }

    @Override
    public void spawn() {
        double part = safeDivide(360 , particleAmount);
        for(double i = 0; i < 360; i += part){
            double rad = Math.toRadians(i);
            double x = Math.cos(rad) * radius;
            double z = Math.sin(rad) * radius;
            Vector vec = rotate(new Vector(x, 0, z));
            spawnParticle(location.clone().add(vec));
        }
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
