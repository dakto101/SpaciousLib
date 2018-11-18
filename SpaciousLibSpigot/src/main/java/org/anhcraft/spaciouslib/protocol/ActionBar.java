package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.*;

/**
 * A class helps you to send action bar message packets
 */
public class ActionBar {
    /**
     * Creates an action bar message packet<br>
     * Supports raw JSON text.<br>
     * @param text the message
     * @return PacketSender object
     */
    public static PacketSender create(String text) {
        return create(text, 20, 60, 20);
    }

    /**
     * Creates an action bar message packet<br>
     * Supports raw JSON text.
     * @param text the message
     * @param fadeIn the fading in time
     * @param stay the staying time
     * @param fadeOut the fading out time
     * @return PacketSender object
     */
    public static PacketSender create(String text, int fadeIn, int stay, int fadeOut) {
        if(CommonUtils.isValidJSON(text)){
            text = Chat.color(text);
        } else {
            text = "{\"text\": \"" + Chat.color(text) + "\"}";
        }
        Object chatBaseComponent = ReflectionUtils.getStaticMethod("a", ClassFinder.NMS.ChatSerializer,
                new Group<>(
                        new Class<?>[]{String.class},
                        new String[]{text}
                ));

        // 1.11 above
        if(GameVersion.is1_11Above()){
            Object enumActionBar = ReflectionUtils.getEnum("ACTIONBAR", ClassFinder.NMS.EnumTitleAction);
            return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutTitle, new Group<>(
                    new Class<?>[]{ClassFinder.NMS.EnumTitleAction, ClassFinder.NMS.IChatBaseComponent,
                            int.class, int.class, int.class},
                    new Object[]{enumActionBar, chatBaseComponent, fadeIn, stay, fadeOut}
            )));
        }
        // 1.8 R1 - 1.10
        else{
            return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutChat, new Group<>(
                    new Class<?>[]{ClassFinder.NMS.IChatBaseComponent, byte.class},
                    new Object[]{chatBaseComponent, (byte) 2}
            )));
        }
    }
}
