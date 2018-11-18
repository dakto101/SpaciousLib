package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.*;

/**
 * A class helps you to send title/subtitle packets
 */
public class Title {
    /**
     * Creates a specific title packet<br>
     * Supports raw JSON text.
     * @param text the message
     * @param type the type of the title
     * @return PacketSender object
     */
    public static PacketSender create(String text, Type type) {
        return create(text, type, 20, 60, 20);
    }

    /**
     * Creates a specific title packet<br>
     * Supports raw JSON text.
     * @param text the message
     * @param type the type of the title
     * @param fadeIn the fading in time
     * @param stay the staying time
     * @param fadeOut the fading out time
     * @return PacketSender object
     */
    public static PacketSender create(String text, Type type, int fadeIn, int stay, int fadeOut) {
        if(CommonUtils.isValidJSON(text)){
            text = Chat.color(text);
        } else {
            text = "{\"text\": \"" + Chat.color(text) + "\"}";
        }
        Object chatBaseComponent = ReflectionUtils.getStaticMethod("a", ClassFinder.NMS.ChatSerializer,
                new Group<>(
                        new Class<?>[]{String.class},
                        new String[]{text}
                ));
        Object enumTitle = ReflectionUtils.getEnum(type.toString(), ClassFinder.NMS.EnumTitleAction);
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutTitle, new Group<>(
                new Class<?>[]{ClassFinder.NMS.EnumTitleAction, ClassFinder.NMS.IChatBaseComponent,
                        int.class, int.class, int.class},
                new Object[]{enumTitle, chatBaseComponent, fadeIn, stay, fadeOut}
        )));
    }

    public enum Type {
        TITLE,
        SUBTITLE,
        TIMES,
        CLEAR,
        RESET
    }
}
