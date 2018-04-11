package org.anhcraft.spaciouslib.world;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A class helps you to manage regions
 */
public class CuboidManager {
    private Location pos1;
    private Location pos2;
    
    /**
     * Creates a new RegionManager instance
     * @param pos1 the first location
     * @param pos2 the second location
     */
    public CuboidManager(Location pos1, Location pos2) throws Exception {
        if(pos1.getWorld() != pos2.getWorld()){
            throw new Exception("Regions must be in a same world");
        }
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    /**
     * Gets the size of this region
     * @return the size
     */
    public double getSize(){
        if (this.pos1 == null && this.pos2 == null) {
            return 0;
        }
        if ((this.pos1 == null) || (this.pos2 == null)) {
            return 1;
        }
        double x = this.pos2.getX() - this.pos1.getX();
        double y = this.pos2.getY() - this.pos1.getY();
        double z = this.pos2.getZ() - this.pos1.getZ();
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
        if (this.pos2.getX() != this.pos1.getX()) {
            x += 1.0D;
        }
        if (this.pos2.getY() != this.pos1.getY()) {
            y += 1.0D;
        }
        if (this.pos2.getZ() != this.pos1.getZ()) {
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
        double x1 = this.pos1.getX();
        double y1 = this.pos1.getY();
        double z1 = this.pos1.getZ();
        double x2 = this.pos2.getX();
        double y2 = this.pos2.getY()+1;
        double z2 = this.pos2.getZ();
        if(x2 < x1){
            for(double x = x2; x < x1+1; x++){
                if(y1 < y2) {
                    for(double y = y1; y < y2; y++) {
                        if(z2 < z1) {
                            for(double z = z2; z < z1 + 1; z++) {
                                loc.add(new Location(this.pos1.getWorld(), x, y, z));
                            }
                        } else {
                            for(double z = z1; z < z2 + 1; z++) {
                                loc.add(new Location(this.pos1.getWorld(), x, y, z));
                            }
                        }
                    }
                } else {
                    for(double y = y2; y < y1; y++) {
                        if(z2 < z1) {
                            for(double z = z2; z < z1 + 1; z++) {
                                loc.add(new Location(this.pos1.getWorld(), x, y, z));
                            }
                        } else {
                            for(double z = z1; z < z2 + 1; z++) {
                                loc.add(new Location(this.pos1.getWorld(), x, y, z));
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
                                loc.add(new Location(this.pos1.getWorld(), x, y, z));
                            }
                        } else {
                            for(double z = z1; z < z2 + 1; z++) {
                                loc.add(new Location(this.pos1.getWorld(), x, y, z));
                            }
                        }
                    }
                } else {
                    for(double y = y2; y < y1; y++) {
                        if(z2 < z1) {
                            for(double z = z2; z < z1 + 1; z++) {
                                loc.add(new Location(this.pos1.getWorld(), x, y, z));
                            }
                        } else {
                            for(double z = z1; z < z2 + 1; z++) {
                                loc.add(new Location(this.pos1.getWorld(), x, y, z));
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
            for(Entity entity : this.pos2.getWorld().getEntities()) {
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