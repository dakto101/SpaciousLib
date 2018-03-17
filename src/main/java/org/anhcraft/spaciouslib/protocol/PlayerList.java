package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GVersion;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.JSONUtils;
import org.anhcraft.spaciouslib.utils.Strings;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayerList {
    public PacketSender create(String header, String footer){
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
            Class<?> chatBaseComponentClass = Class.forName("net.minecraft.server." + v.toString() + ".IChatBaseComponent");
            Method chatSerializer = chatSerializerClass.getDeclaredMethod("a", String.class);
            Object chatBaseComponentHeader = chatSerializer.invoke(null, header);
            Object chatBaseComponentFooter = chatSerializer.invoke(null, footer);
            Class<?> packetPlayOutPlayerListHeaderFooterClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".PacketPlayOutPlayerListHeaderFooter");
            Constructor<?> packetCons = packetPlayOutPlayerListHeaderFooterClass.getDeclaredConstructor(chatBaseComponentClass, chatBaseComponentClass);
            Object packet = packetCons.newInstance(chatBaseComponentHeader, chatBaseComponentFooter);
            return new PacketSender(packet);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void remove(){
        create("{\"translate\":\"\"}", "{\"translate\":\"\"}");
    }
}
