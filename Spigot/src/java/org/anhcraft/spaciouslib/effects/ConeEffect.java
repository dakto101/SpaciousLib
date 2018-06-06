package org.anhcraft.spaciouslib.effects;

import org.anhcraft.spaciouslib.scheduler.DelayedTask;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ConeEffect extends Effect {
    private double radius;
    private double height;

    public ConeEffect(Location location) {
        super(location);
        radius = 3;
        height = 6;
    }

    @Override
    public void spawn() {
        new DelayedTask(() -> {
            double part = safeDivide(360 * height, particleAmount);
            for(double i = 0; i < 360 * height; i+= part) {
                double rad = Math.toRadians(i);
                double x = Math.cos(rad) * radius / (i / 360);
                double z = Math.sin(rad) * radius / (i / 360);
                spawnParticle(location.clone().add(rotate(new Vector(x,i/360,z))));
            }
        }, 0).run();
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
