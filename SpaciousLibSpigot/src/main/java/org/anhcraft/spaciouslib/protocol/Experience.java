package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.ExceptionThrower;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;

public class Experience {
    /**
     * Creates an experience packet
     * @param expBar the current progress for the experience bar (between 0.0 and 1.0)
     * @param level the current level
     * @param totalExp the total amount of experience
     * @return PacketSender object
     */
    public static PacketSender create(float expBar, int level, int totalExp){
        ExceptionThrower.ifTrue(expBar < 0 || expBar > 1, new Exception("the exp progress must be between 0.0 and 1.0"));
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutExperience, new Group<>(
                new Class<?>[]{float.class, int.class, int.class},
                new Object[]{expBar, level, totalExp}
        )));
    }
}
