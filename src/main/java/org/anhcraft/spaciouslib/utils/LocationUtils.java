package org.anhcraft.spaciouslib.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {
    public static Location str2loc(String str) {
        if(str.equalsIgnoreCase("null")){
            return Bukkit.getServer().getWorld("world").getSpawnLocation();
        }
        String str2loc[] = str.split(":");
        Location loc = new Location(Bukkit.getServer().getWorld(str2loc[0]), 0, 0, 0);
        loc.setX(MathUtils.round(Double.parseDouble(str2loc[1])));
        loc.setY(MathUtils.round(Double.parseDouble(str2loc[2])));
        loc.setZ(MathUtils.round(Double.parseDouble(str2loc[3])));
        loc.setPitch((float) MathUtils.round(Double.parseDouble(str2loc[4])));
        loc.setYaw((float) MathUtils.round(Double.parseDouble(str2loc[5])));
        return loc;
    }

    public static List<Block> getNearbyBlocks(Location loc, int rx, int ry, int rz){
        List<Block> list = new ArrayList<>();
        for (int x = -rx; x <= rx; x++){
            for (int y = -ry; y <= ry; y++) {
                for (int z = -rz; z <= rz; z++) {
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

    public static String loc2str(Location loc) {
        return loc.getWorld().getName() + ":" + Double.toString(loc.getX()) + ":" +
                Double.toString(loc.getY()) + ":" + Double.toString(loc.getZ()) +
                ":" + Float.toString(loc.getPitch()) + ":" + Float.toString(loc.getYaw());
    }

    public static boolean isUnderBlock(Player p) {
        Location l = p.getLocation();
        if(l.getY() > 256){
            return false;
        }
        for(int i = l.getBlockY(); i < 256; i++){
            l.setY(i);
            if(!l.getBlock().getType().equals(Material.AIR)){
                return true;
            }
        }
        return false;
    }
}
