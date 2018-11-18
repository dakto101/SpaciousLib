package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.inventory.ItemStack;

public class EntityEquipment {
    public static PacketSender create(int id, EquipSlot slot, ItemStack item){
        if(GameVersion.is1_9Above()) {
            return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutEntityEquipment, new Group<>(
                    new Class<?>[]{int.class, ClassFinder.NMS.EnumItemSlot, ClassFinder.NMS.ItemStack},
                    new Object[]{id, ReflectionUtils.getEnum(slot.toString(), ClassFinder.NMS.EnumItemSlot), ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{item}))}
            )));
        } else {
            int s;
            switch(slot){
                case MAINHAND:
                    s = 0;
                    break;
                case HEAD:
                    s = 1;
                    break;
                case CHEST:
                    s = 2;
                    break;
                case LEGS:
                    s = 3;
                    break;
                case FEET:
                    s = 4;
                    break;
                default:
                    return null;
            }
            return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutEntityEquipment, new Group<>(
                    new Class<?>[]{int.class, int.class, ClassFinder.NMS.ItemStack},
                    new Object[]{id, s, ReflectionUtils.getStaticMethod("asNMSCopy", ClassFinder.CB.CraftItemStack, new Group<>(new Class<?>[]{ItemStack.class}, new Object[]{item}))}
            )));
        }
    }
}
