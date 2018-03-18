package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GVersion;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.JSONUtils;
import org.anhcraft.spaciouslib.utils.Strings;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Chat {
    public static PacketSender create(String text, Type type) {
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
            Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".PacketPlayOutChat");
            Constructor<?> packetCons = packetPlayOutChatClass.getDeclaredConstructor(chatBaseComponentClass, byte.class);
            Object packet = packetCons.newInstance(chatBaseComponent, (byte) type.getID());
            return new PacketSender(packet);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum Type{
        CHAT_BOX(0),
        SYSTEM_MESSAGE(1),
        GAME_INFO(2);

        private int id;

        Type(int id){
            this.id = id;
        }

        public int getID(){
            return id;
        }
    }
}
