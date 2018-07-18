package org.anhcraft.spaciouslib.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

    /**
     * Serializes the given location to string
     * @param loc Location object
     * @return a string represents for the object
     */
    public static String loc2str(Location loc) {
        if(loc == null){
            return "null";
        }
        return loc.getWorld().getName() + ":" + Double.toString(loc.getX()) + ":" +
                Double.toString(loc.getY()) + ":" + Double.toString(loc.getZ()) +
                ":" + Float.toString(loc.getPitch()) + ":" + Float.toString(loc.getYaw());
    }

    /**
     * Deserialize the given string to its location
     * @param str a string represents for the original location object
     * @return the location
     */
    public static Location str2loc(String str) {
        if(str.equalsIgnoreCase("null")){
            return Bukkit.getServer().getWorld("world").getSpawnLocation();
        }
        String str2loc[] = str.split(":");
        Location loc = new Location(Bukkit.getServer().getWorld(str2loc[0]), 0, 0, 0);
        loc.setX(Double.parseDouble(str2loc[1]));
        loc.setY(Double.parseDouble(str2loc[2]));
        loc.setZ(Double.parseDouble(str2loc[3]));
        loc.setPitch(Float.parseFloat(str2loc[4]));
        loc.setYaw(Float.parseFloat(str2loc[5]));
        return loc;
    }

    /**
     * Gets nearby locations
     * @param loc the center location
     * @param rx distance on the x axis
     * @param ry distance on the y axis
     * @param rz distance on the z axis
     * @return a list of all nearby locations
     */
    public static List<Location> getNearbyLocations(Location loc, int rx, int ry, int rz){
        List<Location> list = new ArrayList<>();
        for (int x = -(rx); x <= rx; x++){
            for (int y = -(ry); y <= ry; y++) {
                for (int z = -(rz); z <= rz; z++) {
                    list.add(new Location(
                            loc.getWorld(),
                            loc.getX() + x,
                            loc.getY() + y,
                            loc.getZ() + z));
                }
            }
        }
        return list;
    }

    /**
     * Gets nearby blocks
     * @param loc the center location
     * @param rx distance on the x axis
     * @param ry distance on the y axis
     * @param rz distance on the z axis
     * @return a list of all nearby blocks
     */
    public static List<Block> getNearbyBlocks(Location loc, int rx, int ry, int rz){
        List<Block> list = new ArrayList<>();
        for (int x = -(rx); x <= rx; x++){
            for (int y = -(ry); y <= ry; y++) {
                for (int z = -(rz); z <= rz; z++) {
                    list.add(new Location(
                            loc.getWorld(),
                            loc.getX() + x,
                            loc.getY() + y,
                            loc.getZ() + z).getBlock());
                }
            }
        }
        return list;
    }

    /**
     * Check is the given location under a block
     * @param loc a location
     * @return true if yes
     */
    public static boolean isUnderBlock(Location loc) {
        if(loc.getY() > 256){
            return false;
        }
        for(int i = loc.getBlockY(); i < 256; i++){
            loc.setY(i);
            if(!loc.getBlock().getType().equals(Material.AIR)){
                return true;
            }
        }
        return false;
    }
}
