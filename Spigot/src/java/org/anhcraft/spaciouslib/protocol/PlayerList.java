package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.*;

/**
 * A class helps you to send PlayerListHeaderFooter packet
 */
public class PlayerList {
    /**
     * Creates a PlayerListHeaderFooter packet<br>
     * Supports raw JSON text.
     * @param header the header
     * @param footer the footer
     * @return PacketSender object
     */
    public static PacketSender create(String header, String footer){
        if(CommonUtils.isValidJSON(header)){
            header = Chat.color(header);
        } else {
            header = "{\"text\": \"" + Chat.color(header) + "\"}";
        }
        if(CommonUtils.isValidJSON(footer)){
            footer = Chat.color(footer);
        } else {
            footer = "{\"text\": \"" + Chat.color(footer) + "\"}";
        }
        GameVersion v = GameVersion.getVersion();
        try {
            Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + v.toString() + "." + (v.equals(GameVersion.v1_8_R1) ? "" : "IChatBaseComponent$") + "ChatSerializer");
            Class<?> packetPlayOutPlayerListHeaderFooterClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".PacketPlayOutPlayerListHeaderFooter");
            Object chatBaseComponentHeader = ReflectionUtils.getStaticMethod("a", chatSerializerClass,
                    new Group<>(
                            new Class<?>[]{String.class},
                            new String[]{header}
                    ));
            Object chatBaseComponentFooter = ReflectionUtils.getStaticMethod("a", chatSerializerClass,
                    new Group<>(
                            new Class<?>[]{String.class},
                            new String[]{footer}
                    ));
            Object packet = ReflectionUtils.getConstructor(packetPlayOutPlayerListHeaderFooterClass);
            ReflectionUtils.setField("a", packetPlayOutPlayerListHeaderFooterClass, packet, chatBaseComponentHeader);
            ReflectionUtils.setField("b", packetPlayOutPlayerListHeaderFooterClass, packet, chatBaseComponentFooter);
            return new PacketSender(packet);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes the player list<br>
     * Supports raw JSON text.
     * @return PacketSender object
     */
    public static PacketSender remove(){
        return create("{\"translate\":\"\"}", "{\"translate\":\"\"}");
    }
}
