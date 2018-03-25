package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GVersion;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.JSONUtils;
import org.anhcraft.spaciouslib.utils.Strings;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Title {
    public static PacketSender create(String text, Type type) {
        return create(text, type, 20, 60, 20);
    }

    public static PacketSender create(String text, Type type, int fadeIn, int stay, int fadeOut) {
        if(JSONUtils.isValid(text)){
            text = Strings.color(text);
        } else {
            text = "{\"text\": \"" + Strings.color(text) + "\"}";
        }
        GVersion v = GameVersion.getVersion();
        try {
            Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + v.toString() + "." + (v.equals(GVersion.v1_8_R1) ? "" : "IChatBaseComponent$") + "ChatSerializer");
            Class<?> chatBaseComponentClass = Class.forName("net.minecraft.server." + v.toString() + ".IChatBaseComponent");
            Method chatSerializer = chatSerializerClass.getDeclaredMethod("a", String.class);
            Object chatBaseComponent = chatSerializer.invoke(null, text);

            Class<?> packetPlayOutTitleClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".PacketPlayOutTitle");
            Class<?> enumTitleActionClass = Class.forName("net.minecraft.server." + v.toString() + "."+(v.equals(GVersion.v1_8_R1) ? "" : "PacketPlayOutTitle$")+"EnumTitleAction");
            Field enumTitleField = enumTitleActionClass.getDeclaredField(type.toString());
            enumTitleField.setAccessible(true);
            Object enumTitle = enumTitleField.get(null);
            Constructor<?> packetCons = packetPlayOutTitleClass.getDeclaredConstructor(enumTitleActionClass, chatBaseComponentClass, int.class, int.class, int.class);
            Object packet = packetCons.newInstance(enumTitle, chatBaseComponent, fadeIn, stay, fadeOut);
            return new PacketSender(packet);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e) {
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
