package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;

public class WindowData {
    /**
     * Creates a window property packet
     * @param id the id of a window
     * @param property the property
     * @param value a new value
     * @return PacketSender object
     */
    public static PacketSender create(int id, int property, int value) {
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> packetPlayOutWindowDataClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutWindowData");
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutWindowDataClass, new Group<>(
                    new Class<?>[]{int.class, int.class, int.class},
                    new Object[]{id, property, value}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
