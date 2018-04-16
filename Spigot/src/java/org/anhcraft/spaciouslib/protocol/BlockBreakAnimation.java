package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.block.Block;

/**
 * A class helps you to send block-breaking animation packets
 */
public class BlockBreakAnimation {
    /**
     * Creates a block-breaking animation packet
     * @param id the unique id for the packet
     * @param block the block
     * @param stage the stage value
     * @return PacketSender object
     */
    public static PacketSender create(int id, Block block, int stage) {
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> blockPositionClass = Class.forName("net.minecraft.server." + v + ".BlockPosition");
            Class<?> packetPlayOutBlockBreakAnimationClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutBlockBreakAnimation");
            Object blockPosition = ReflectionUtils.getConstructor(blockPositionClass, new Group<>(
                    new Class<?>[]{int.class, int.class, int.class},
                    new Object[]{block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()}
            ));
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutBlockBreakAnimationClass, new Group<>(
                    new Class<?>[]{int.class, blockPositionClass, int.class},
                    new Object[]{id, blockPosition, stage}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
