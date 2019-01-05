package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.mojang.Skin;
import org.anhcraft.spaciouslib.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BungeeListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String s, Player x, byte[] bytes) {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            if(s.equals(SpaciousLib.SL_CHANNEL)){
                String sc = data.readUTF();
                switch(sc) {
                    case "skin":
                        PlayerUtils.changeSkin(Bukkit.getServer().getPlayer(data.readUTF()),
                                new Skin(data.readUTF(), data.readUTF()));
                        break;
                    case "cmdsv":
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), data.readUTF());
                        break;
                    case "playercmdsv":
                        String cmd2 = data.readUTF();
                        String player2 = data.readUTF();
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getPlayer(player2), cmd2);
                        break;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
