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
        GameVersion v = GameVersion.getVersion();
        try {
            Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + v.toString() + "." + (v.equals(GameVersion.v1_8_R1) ? "" : "IChatBaseComponent$") + "ChatSerializer");
            Class<?> chatBaseComponentClass = Class.forName("net.minecraft.server." + v.toString() + ".IChatBaseComponent");
            Object chatBaseComponent = ReflectionUtils.getStaticMethod("a", chatSerializerClass,
                    new Group<>(
                            new Class<?>[]{String.class},
                            new String[]{text}
                    ));

            Class<?> packetPlayOutTitleClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".PacketPlayOutTitle");
            Class<?> enumTitleActionClass = Class.forName("net.minecraft.server." + v.toString() + "."+(v.equals(GameVersion.v1_8_R1) ? "" : "PacketPlayOutTitle$")+"EnumTitleAction");
            Object enumTitle = ReflectionUtils.getEnum(type.toString(), enumTitleActionClass);
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutTitleClass, new Group<>(
                    new Class<?>[]{enumTitleActionClass, chatBaseComponentClass,
                            int.class, int.class, int.class},
                    new Object[]{enumTitle, chatBaseComponent, fadeIn, stay, fadeOut}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum Type {
        TITLE,
        SUBTITLE,
        TIMES,
        CLEAR,
        RESET
    }
}
