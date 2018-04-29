package org.anhcraft.spaciouslib.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class CuboidUtils {
    /**
     * Gets the volume of a cuboid
     * @param pos1 the top corner of the cuboid
     * @param pos2 the bottom corner which is opposite the top corner of the cuboid
     * @return the volume
     */
    public static double getVolume(Location pos1, Location pos2) throws Exception {
        if(pos1.getWorld() != pos2.getWorld()){
            throw new Exception("The locations must be in a same world");
        }
        if (pos1 == null || pos2 == null) {
            return 0;
        }
        double x = pos2.getX() - pos1.getX();
        double y = pos2.getY() - pos1.getY();
        double z = pos2.getZ() - pos1.getZ();
        if (x < 0.0D) {
            x = -x;
        }
        if (y < 0.0D) {
            y = -y;
        }
        if (z < 0.0D) {
            z = -z;
        }
        x += 1.0D;
        y += 1.0D;
        z += 1.0D;
        return x * y * z;
    }

    /**
     * Gets all locations inside a cuboid
     * @param pos1 the top corner of the cuboid
     * @param pos2 the bottom corner which is opposite the top corner of the cuboid
     * @return list of locations
     */
    public static List<Location> getLocations(Location pos1, Location pos2) throws Exception {
        if(pos1.getWorld() != pos2.getWorld()){
            throw new Exception("The locations must be in a same world");
        }
        List<Location> loc = new ArrayList<>();
        double x1 = pos1.getX();
        double y1 = pos1.getY();
        double z1 = pos1.getZ();
        double x2 = pos2.getX();
        double y2 = pos2.getY()+1;
        double z2 = pos2.getZ();
        if(x2 < x1){
            for(double x = x2; x < x1+1; x++){
                if(y1 < y2) {
                    for(double y = y1; y < y2; y++) {
                        if(z2 < z1) {
                            for(double z = z2; z < z1 + 1; z++) {
                                loc.add(new Location(pos1.getWorld(), x, y, z));
                            }
                        } else {
                            for(double z = z1; z < z2 + 1; z++) {
                                loc.add(new Location(pos1.getWorld(), x, y, z));
                            }
                        }
                    }
                } else {
                    for(double y = y2; y < y1; y++) {
                        if(z2 < z1) {
                            for(double z = z2; z < z1 + 1; z++) {
                                loc.add(new Location(pos1.getWorld(), x, y, z));
                            }
                        } else {
                            for(double z = z1; z < z2 + 1; z++) {
                                loc.add(new Location(pos1.getWorld(), x, y, z));
                            }
                        }
                    }
                }
            }
        }else {
            for(double x = x1; x < x2+1; x++){
                if(y1 < y2) {
                    for(double y = y1; y < y2; y++) {
                        if(z2 < z1) {
                            for(double z = z2; z < z1 + 1; z++) {
                                loc.add(new Location(pos1.getWorld(), x, y, z));
                            }
                        } else {
                            for(double z = z1; z < z2 + 1; z++) {
                                loc.add(new Location(pos1.getWorld(), x, y, z));
                            }
                        }
                    }
                } else {
                    for(double y = y2; y < y1; y++) {
                        if(z2 < z1) {
                            for(double z = z2; z < z1 + 1; z++) {
                                loc.add(new Location(pos1.getWorld(), x, y, z));
                            }
                        } else {
                            for(double z = z1; z < z2 + 1; z++) {
                                loc.add(new Location(pos1.getWorld(), x, y, z));
                            }
                        }
                    }
                }
            }
        }
        return loc;
    }

    /**
     * Gets all entities inside a cuboid
     * @param pos1 the top corner of the cuboid
     * @param pos2 the bottom corner which is opposite the top corner of the cuboid
     * @return list of entities
     */
    public static List<Entity> getEntities(Location pos1, Location pos2) throws Exception {
        if(pos1.getWorld() != pos2.getWorld()){
            throw new Exception("The locations must be in a same world");
        }
        List<Entity> e = new ArrayList<>();
        for(Location loc : getLocations(pos1, pos2)){
            for(Entity entity : pos2.getWorld().getEntities()) {
                if(loc.getBlockX() == entity.getLocation().getBlockX()
                        && loc.getBlockY() == entity.getLocation().getBlockY()
                        && loc.getBlockZ() == entity.getLocation().getBlockZ()) {
                    e.add(entity);
                }
            }
        }
        return e;
    }
}