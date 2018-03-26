package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GVersion;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.JSONUtils;
import org.anhcraft.spaciouslib.utils.Strings;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        if(JSONUtils.isValid(header)){
            header = Strings.color(header);
        } else {
            header = "{\"text\": \"" + Strings.color(header) + "\"}";
        }
        if(JSONUtils.isValid(footer)){
            footer = Strings.color(footer);
        } else {
            footer = "{\"text\": \"" + Strings.color(footer) + "\"}";
        }
        GVersion v = GameVersion.getVersion();
        try {
            Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + v.toString() + "." + (v.equals(GVersion.v1_8_R1) ? "" : "IChatBaseComponent$") + "ChatSerializer");
            Class<?> packetPlayOutPlayerListHeaderFooterClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".PacketPlayOutPlayerListHeaderFooter");
            Method chatSerializer = chatSerializerClass.getDeclaredMethod("a", String.class);
            Object chatBaseComponentHeader = chatSerializer.invoke(null, header);
            Object chatBaseComponentFooter = chatSerializer.invoke(null, footer);
            Constructor<?> packetCons = packetPlayOutPlayerListHeaderFooterClass.getDeclaredConstructor();
            Object packet = packetCons.newInstance();
            Field headerField = packetPlayOutPlayerListHeaderFooterClass.getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, chatBaseComponentHeader);
            Field footerField = packetPlayOutPlayerListHeaderFooterClass.getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, chatBaseComponentFooter);
            return new PacketSender(packet);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e) {
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
