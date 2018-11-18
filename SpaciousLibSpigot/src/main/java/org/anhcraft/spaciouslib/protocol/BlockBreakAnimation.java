package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * A class helps you to send block-breaking animation packets
 */
public class BlockBreakAnimation {
    /**
     * Creates a block-breaking animation packet
     * @param id a unique id for the packet
     * @param block a block
     * @param stage the stage of breaking
     * @return PacketSender object
     */
    public static PacketSender create(int id, Block block, int stage) {
        if(stage < 0 || stage > 9){
            stage = 9;
        }
        Object blockPosition = ReflectionUtils.getConstructor(ClassFinder.NMS.BlockPosition, new Group<>(
                new Class<?>[]{int.class, int.class, int.class},
                new Object[]{block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()}
        ));
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutBlockBreakAnimation, new Group<>(
                new Class<?>[]{int.class, ClassFinder.NMS.BlockPosition, int.class},
                new Object[]{id, blockPosition, stage}
        )));
    }

    /**
     * Creates a block-breaking animation packet
     * @param id a unique id for the packet
     * @param location a location
     * @param stage the stage of breaking
     * @return PacketSender object
     */
    public static PacketSender create(int id, Location location, int stage) {
        return create(id, location.getBlock(), stage);
    }

    /**
     * Removes an existed block-breaking animation
     * @param id the unique id of the animation
     * @param location the location which was showed on
     * @return PacketSender object
     */
    public static PacketSender remove(int id, Location location) {
        return create(id, location.getBlock(), 10);
    }

    /**
     * Removes an existed block-breaking animation
     * @param id the unique id of the animation
     * @param block the block which was showed on
     * @return PacketSender object
     */
    public static PacketSender remove(int id, Block block) {
        return create(id, block, 10);
    }
}
