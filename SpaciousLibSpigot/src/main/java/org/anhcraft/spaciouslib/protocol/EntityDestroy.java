package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;

public class EntityDestroy {
    public static PacketSender create(int entityID){
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> packetPlayOutEntityDestroyClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutEntityDestroy");
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutEntityDestroyClass, new Group<>(
                    new Class<?>[]{int[].class},
                    new Object[]{new int[]{entityID}}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
