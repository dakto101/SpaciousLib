package org.anhcraft.spaciouslib.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.anhcraft.spaciouslib.inventory.HandSlot;
import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;

public class CustomPayload {
    public static PacketSender create(String channel, ByteBuf buf){
        Object data = ReflectionUtils.getConstructor(ClassFinder.NMS.PacketDataSerializer, new Group<>(
                new Class<?>[]{ByteBuf.class},
                new Object[]{buf}
        ));
        if(GameVersion.is1_13Above()){
            Object minecraftKey = ReflectionUtils.getConstructor(ClassFinder.NMS.MinecraftKey, new Group<>(
                    new Class<?>[]{String.class},
                    new Object[]{channel}
            ));
            return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutCustomPayload, new Group<>(
                    new Class<?>[]{ClassFinder.NMS.MinecraftKey,ClassFinder.NMS.PacketDataSerializer},
                    new Object[]{minecraftKey, data}
            )));
        } else {
            return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutCustomPayload, new Group<>(
                    new Class<?>[]{String.class,ClassFinder.NMS.PacketDataSerializer},
                    new Object[]{channel, data}
            )));
        }
    }

    public static PacketSender openBook(HandSlot hand){
        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, hand == HandSlot.MAINHAND ? 0 : 1);
        buf.writerIndex(1);
        return create(GameVersion.is1_13Above() ? "minecraft:book_open" : "MC|BOpen", buf);
    }
}
