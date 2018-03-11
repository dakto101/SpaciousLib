package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GVersion;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Strings;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActionBar {
    public static PacketSender create(String text) {
        return create(text, 20, 60, 20);
    }

    public static PacketSender create(String text, int fadeIn, int stay, int fadeOut) {
        text = "{\"text\": \"" + Strings.color(text) + "\"}";
        GVersion v = GameVersion.getVersion();
        try {
            Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + v.toString() + "." + (v.equals(GVersion.v1_8_R1) ? "" : "IChatBaseComponent$") + "ChatSerializer");
            Class<?> chatBaseComponentClass = Class.forName("net.minecraft.server." + v.toString() + ".IChatBaseComponent");
            Method chatSerializer = chatSerializerClass.getDeclaredMethod("a", String.class);
            Object chatBaseComponent = chatSerializer.invoke(null, text);

            // 1.11 - 1.12
            if(v.equals(GVersion.v1_11_R1) || v.equals(GVersion.v1_12_R1)){
                Class<?> packetPlayOutTitleClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".PacketPlayOutTitle");
                Class<?> enumTitleActionClass = Class.forName("net.minecraft.server." + v.toString() + ".PacketPlayOutTitle$EnumTitleAction");
                Field enumActionBarField = enumTitleActionClass.getDeclaredField("ACTIONBAR");
                enumActionBarField.setAccessible(true);
                Object enumActionBar = enumActionBarField.get(null);
                Constructor<?> packetCons = packetPlayOutTitleClass.getDeclaredConstructor(enumTitleActionClass, chatBaseComponentClass, int.class, int.class, int.class);
                Object packet = packetCons.newInstance(enumActionBar, chatBaseComponent, fadeIn, stay, fadeOut);
                return new PacketSender(packet);
            }
            // 1.8 R1 - 1.10
            else{
                Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".PacketPlayOutChat");
                Constructor<?> packetCons = packetPlayOutChatClass.getDeclaredConstructor(chatBaseComponentClass, byte.class);
                Object packet = packetCons.newInstance(chatBaseComponent, (byte) 2);
                return new PacketSender(packet);
            }
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
