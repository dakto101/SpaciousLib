package org.anhcraft.spaciouslib.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.mojang.Skin;

public class PlayerUtils {
    /**
     * Get the ping number of the given player at the moment
     * @param player player
     * @return number of ping
     */
    public static int getPing(ProxiedPlayer player){
        return player.getPing();
    }

    /**
     * Change the given player's skin.<br>
     * This method works in the destination server and only affects any viewers within the defined view distance.
     * @param player the player
     * @param skin new skin
     */
    public static void changeSkin(ProxiedPlayer player, Skin skin){
        InitialHandler ih = (InitialHandler) player.getPendingConnection();
        LoginResult lr = ih.getLoginProfile();
        if(lr == null) {
            lr = new LoginResult(player.getUniqueId().toString().replace("-", ""),player.getName(), null);
        }
        lr.setProperties(new LoginResult.Property[]{
                new LoginResult.Property("textures", skin.getValue(), skin.getSignature())
        });
        ReflectionUtils.setField("loginProfile", InitialHandler.class, ih, lr);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("skin");
        out.writeUTF(player.getName());
        out.writeUTF(skin.getValue());
        out.writeUTF(skin.getSignature());
        player.getServer().sendData(SpaciousLib.CHANNEL, out.toByteArray());
    }
}
