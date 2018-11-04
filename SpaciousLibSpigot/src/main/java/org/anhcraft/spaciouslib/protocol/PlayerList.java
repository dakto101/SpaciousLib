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
        return build(header, footer);
    }

    private static PacketSender build(String header, String footer) {
        String a = "a";
        String b = "b";
        if(GameVersion.getVersion().equals(GameVersion.v1_13_R2)){
            a = "header";
            b = "footer";
        }
        Object chatBaseComponentHeader = ReflectionUtils.getStaticMethod("a", ClassFinder.NMS.ChatSerializer,
                new Group<>(
                        new Class<?>[]{String.class},
                        new String[]{header}
                ));
        Object chatBaseComponentFooter = ReflectionUtils.getStaticMethod("a", ClassFinder.NMS.ChatSerializer,
                new Group<>(
                        new Class<?>[]{String.class},
                        new String[]{footer}
                ));
        Object packet = ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutPlayerListHeaderFooter);
        ReflectionUtils.setField(a, ClassFinder.NMS.PacketPlayOutPlayerListHeaderFooter, packet, chatBaseComponentHeader);
        ReflectionUtils.setField(b, ClassFinder.NMS.PacketPlayOutPlayerListHeaderFooter, packet, chatBaseComponentFooter);
        return new PacketSender(packet);
    }

    /**
     * Removes the player list<br>
     * Supports raw JSON text.
     * @return PacketSender object
     */
    public static PacketSender remove(){
        return build("{\"translate\":\"\"}", "{\"translate\":\"\"}");
    }
}
