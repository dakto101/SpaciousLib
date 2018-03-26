package org.anhcraft.spaciouslib.region;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A class helps you to manage regions
 */
public class RegionManager {
    private CuboidRegion cr;

    /**
     * Creates a new RegionManager instance
     * @param cr CuboidRegion object
     * @throws Exception
     */
    public RegionManager(CuboidRegion cr) throws Exception {
        if(cr.getPosition1().getWorld() != cr.getPosition2().getWorld()){
            throw new Exception("Regions must be in a same world");
        }
        this.cr = cr;
    }

    /**
     * Gets the size of this region
     * @return the size
     */
    public double getSize(){
        if (cr.getPosition1() == null && cr.getPosition2() == null) {
            return 0;
        }
        if ((cr.getPosition1() == null) || (cr.getPosition2() == null)) {
            return 1;
        }
        double x = cr.getPosition2().getX() - cr.getPosition1().getX();
        double y = cr.getPosition2().getY() - cr.getPosition1().getY();
        double z = cr.getPosition2().getZ() - cr.getPosition1().getZ();
        if (x < 0.0D) {
            x *= -1.0D;
        } else if (x == 0.0D) {
            x = 1.0D;
        }
        if (y < 0.0D) {
            y *= -1.0D;
        } else if (y == 0.0D) {
            y = 1.0D;
        }
        if (z < 0.0D) {
            z *= -1.0D;
        } else if (z == 0.0D) {
            z = 1.0D;
        }
        if (cr.getPosition2().getX() != cr.getPosition1().getX()) {
            x += 1.0D;
        }
        if (cr.getPosition2().getY() != cr.getPosition1().getY()) {
            y += 1.0D;
        }
        if (cr.getPosition2().getZ() != cr.getPosition1().getZ()) {
            z += 1.0D;
        }
        return x * y * z;
    }

    /**
     * Gets all locations in this region
     * @return list of locations
     */
    public List<Location> getLocations(){
        List<Location> loc = new ArrayList<>();
        double x1 = cr.getPosition1().getX();
        double y1 = cr.getPosition1().getY();
        double z1 = cr.getPosition1().getZ();
        double x2 = cr.getPosition2().getX();
        double y2 = cr.getPosition2().getY()+1;
        double z2 = cr.getPosition2().getZ();
        if(x2 < x1){
            for(double x = x2; x < x1+1; x++){
                if(y1 < y2) {
                    for(double y = y1; y < y2; y++) {
                        if(z2 < z1) {
                            for(double z = z2; z < z1 + 1; z++) {
                                loc.add(new Location(cr.getPosition1().getWorld(), x, y, z));
                            }
                        } else {
                            for(double z = z1; z < z2 + 1; z++) {
                                loc.add(new Location(cr.getPosition1().getWorld(), x, y, z));
                            }
                        }
                    }
                } else {
                    for(double y = y2; y < y1; y++) {
                        if(z2 < z1) {
                            for(double z = z2; z < z1 + 1; z++) {
                                loc.add(new Location(cr.getPosition1().getWorld(), x, y, z));
                            }
                        } else {
                            for(double z = z1; z < z2 + 1; z++) {
                                loc.add(new Location(cr.getPosition1().getWorld(), x, y, z));
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
                                loc.add(new Location(cr.getPosition1().getWorld(), x, y, z));
                            }
                        } else {
                            for(double z = z1; z < z2 + 1; z++) {
                                loc.add(new Location(cr.getPosition1().getWorld(), x, y, z));
                            }
                        }
                    }
                } else {
                    for(double y = y2; y < y1; y++) {
                        if(z2 < z1) {
                            for(double z = z2; z < z1 + 1; z++) {
                                loc.add(new Location(cr.getPosition1().getWorld(), x, y, z));
                            }
                        } else {
                            for(double z = z1; z < z2 + 1; z++) {
                                loc.add(new Location(cr.getPosition1().getWorld(), x, y, z));
                            }
                        }
                    }
                }
            }
        }
        return loc;
    }

    /**
     * Gets all entities in this region
     * @return list of entities
     */
    public List<Entity> getEntities(){
        List<Entity> e = new ArrayList<>();
        for(Location loc : getLocations()){
            for(Entity entity : cr.getPosition2().getWorld().getEntities()) {
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