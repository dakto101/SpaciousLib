package org.anhcraft.spaciouslib.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {
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
}
