package org.anhcraft.spaciouslib.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;

public class BlockUtils {
    /**
     * Gets the face of the block which is being targeted by a player.<br>
     * Source: https://www.spigotmc.org/threads/getting-the-blockface-of-a-targeted-block.319181/#post-3003022
     *
     * @param player a player
     * @author Benz56
     * @return the face of the block
     */
    public BlockFace getBlockFace(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(new HashSet<Material>(), 100);
        if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()){
            return null;
        }
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        return targetBlock.getFace(adjacentBlock);
    }
}
