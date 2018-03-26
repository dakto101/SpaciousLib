package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GVersion;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.block.Block;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
        GVersion v = GameVersion.getVersion();
        try {
            Class<?> blockPositionClass = Class.forName("net.minecraft.server." + v.toString() + ".BlockPosition");
            Class<?> packetPlayOutBlockBreakAnimationClass = Class.forName("net.minecraft.server." + v.toString() + ".PacketPlayOutBlockBreakAnimation");
            Constructor blockPositionCons = blockPositionClass.getConstructor(int.class, int.class, int.class);
            Object blockPosition = blockPositionCons.newInstance(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
            Constructor<?> packetCons = packetPlayOutBlockBreakAnimationClass.getDeclaredConstructor(int.class, blockPositionClass, int.class);
            Object packet = packetCons.newInstance(id, blockPosition, stage);
            return new PacketSender(packet);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
