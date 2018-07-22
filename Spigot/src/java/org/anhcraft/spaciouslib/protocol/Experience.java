package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.apache.commons.lang3.Validate;

public class Experience {
    /**
     * Creates an experience packet
     * @param expBar the current progress for the experience bar (between 0.0 and 1.0)
     * @param level the current level
     * @param totalExp the total amount of experience
     * @return PacketSender object
     */
    public static PacketSender create(float expBar, int level, int totalExp){
        Validate.isTrue(expBar < 0 || expBar > 1, "the current progress must be between 0.0 and 1.0");
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> packetClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutExperience");
            return new PacketSender(ReflectionUtils.getConstructor(packetClass, new Group<>(
                    new Class<?>[]{float.class, int.class, int.class},
                    new Object[]{expBar, level, totalExp}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
