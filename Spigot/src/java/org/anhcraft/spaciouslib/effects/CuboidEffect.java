package org.anhcraft.spaciouslib.effects;

import org.anhcraft.spaciouslib.scheduler.DelayedTask;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class CuboidEffect extends TargetedEffect {
    public CuboidEffect(Location location, Location target) {
        super(location, target);
    }

    public CuboidEffect(Location location, double width, double height, double deep, boolean alignCenter) {
        super(location, location);
        if(alignCenter) {
            Location a = location.clone();
            a.setX(a.getX() + (width / 2));
            a.setY(a.getY() + (height / 2));
            a.setZ(a.getZ() + (deep / 2));
            setTarget(a);
            Location b = location.clone();
            b.setX(b.getX() - (width / 2));
            b.setY(b.getY() - (height / 2));
            b.setZ(b.getZ() - (deep / 2));
            setLocation(b);
        } else {
            Location a = location.clone();
            a.setX(a.getX() + width);
            a.setY(a.getY() + height);
            a.setZ(a.getZ() + deep);
            setTarget(a);
        }
    }

    @Override
    public void setTarget(Location target){
        this.target = target;
        double width = Math.abs(target.getX() - location.getX());
        double height = Math.abs(target.getY() - location.getY());
        double deep = Math.abs(target.getZ() - location.getZ());
        setParticleAmount(width * height * deep);
    }

    @Override
    public void spawn() {
        new DelayedTask(() -> {
            double width = Math.abs(target.getX() - location.getX());
            double height = Math.abs(target.getY() - location.getY());
            double deep = Math.abs(target.getZ() - location.getZ());
            double ratio = safeDivide(width * height * deep, particleAmount);
            double ratioX = safeDivide(width, height * deep) * ratio;
            double ratioY = safeDivide(height, width * deep) * ratio;
            double ratioZ = safeDivide(deep, width * height) * ratio;
            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    for(int z = 0; z < deep; z++){
                        if(x == 0 || y == 0 || z == 0 || x == width - 1 || y == height - 1 || z == deep - 1){
                            spawnParticle(location.clone().add(rotate(
                                    new Vector(x * ratioX, y * ratioY, z * ratioZ))));
                        }
                    }
                }
            }
        }, 0).run();
    }
}
